/*
 * Copyright (c) 2011-2014 Pivotal Software, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package reactor.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;

/**
 * A factory for creating {@link BlockingQueue} instances. When available, {@link
 * LinkedTransferQueue}s will be created, otherwise {@link LinkedBlockingQueue}s will be
 * created.
 *
 * @author Stephane Maldini
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class BlockingQueueFactory {

	static {
		try {
			QUEUE_TYPE = (Class<? extends BlockingQueue>) Class.forName("java.util.concurrent.LinkedTransferQueue");
		} catch (ClassNotFoundException e) {
			QUEUE_TYPE = LinkedBlockingQueue.class;
		}
	}

	private static Class<? extends BlockingQueue> QUEUE_TYPE;

	/**
	 * Creates a new {@link BlockingQueue}
	 *
	 * @param <D> the type of values that the queue will hold
	 *
	 * @return the blocking queue
	 */
	public static <D> BlockingQueue<D> createQueue() {
		try {
			return (BlockingQueue<D>) QUEUE_TYPE.newInstance();
		} catch (Throwable t) {
			return new LinkedBlockingQueue<D>();
		}
	}
}
