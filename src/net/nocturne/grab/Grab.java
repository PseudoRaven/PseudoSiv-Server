package net.nocturne.grab;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.nocturne.cache.Cache;
import net.nocturne.executor.GrabThread;
import net.nocturne.network.Session;

public class Grab {

	public static final int MAX_REQUEST_SIZE = 500;// was 100 in 831

	private ConcurrentLinkedQueue<ArchiveRequest> priorityArchives;
	private ConcurrentLinkedQueue<ArchiveRequest> nonPriorityArchives;

	private boolean initialized = false;
	private boolean isLoggedIn;

	private Session session;

	public Grab(Session session) {
		this.session = session;
	}

	public void addArchive(int index, int archive, boolean priority) {
		if (index == 255 && archive != 255) {
			if (archive < 0 || archive >= Cache.STORE.getIndexes().length
					|| Cache.STORE.getIndexes()[archive] == null)
				return;
		} else if (index != 255) {
			if (index < 0 || index >= Cache.STORE.getIndexes().length
					|| Cache.STORE.getIndexes()[index] == null || archive < 0
					|| !Cache.STORE.getIndexes()[index].archiveExists(archive))
				return;
		}

		if (!initialized) {
			finish();
			return;
		}

		Queue<ArchiveRequest> requestList = priority ? priorityArchives
				: nonPriorityArchives;
		if (requestList.size() >= MAX_REQUEST_SIZE) // in 876 its 500 for high
													// and low
			return;

		ArchiveRequest req = new ArchiveRequest(index, archive);
		if (requestList.contains(req))
			return;

		synchronized (requestList) {
			requestList.add(req);
		}
	}

	public long processNext(long limit) {
		if (!session.getChannel().isConnected()) {
			finish();
			return 0;
		}
		long total = 0;

		if (priorityArchives.isEmpty()) {
			synchronized (nonPriorityArchives) {
				while (!nonPriorityArchives.isEmpty() && total < limit) {
					ArchiveRequest request = nonPriorityArchives.peek();
					int bytesSent = request.getBytesSent();
					if (session.getGrabPackets().sendCacheArchive(request,
							false))
						nonPriorityArchives.remove(request);
					total += request.getBytesSent() - bytesSent;
				}
			}
		} else {
			synchronized (priorityArchives) {
				while (!priorityArchives.isEmpty() && total < limit) {
					ArchiveRequest request = priorityArchives.peek();
					int bytesSent = request.getBytesSent();
					if (session.getGrabPackets()
							.sendCacheArchive(request, true))
						priorityArchives.remove(request);
					total += request.getBytesSent() - bytesSent;
				}
			}
		}
		return total;
	}

	public void init() {
		if (initialized) {
			finish();
			return;
		}
		priorityArchives = new ConcurrentLinkedQueue<>();// Collections.synchronizedMap(new
		nonPriorityArchives = new ConcurrentLinkedQueue<>();// Collections.synchronizedMap(new

		initialized = true;
		GrabThread.add(this);
	}

	public void finish() {
		session.getChannel().close();
		if (!initialized)
			return;
		GrabThread.remove(this);
		priorityArchives = nonPriorityArchives = null;
		initialized = false;
	}

	public boolean isLoggedIn() {
		return isLoggedIn;
	}

	public void setLoggedIn(boolean clientLoggedIn) {
		if (!initialized) {
			finish();
			return;
		}
		this.isLoggedIn = clientLoggedIn;
	}

	public Session getSession() {
		return session;
	}
}