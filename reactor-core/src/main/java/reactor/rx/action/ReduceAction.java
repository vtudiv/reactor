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
package reactor.rx.action;

import reactor.core.Dispatcher;
import reactor.function.Function;
import reactor.function.Supplier;
import reactor.tuple.Tuple2;

/**
 * @author Stephane Maldini
 * @since 1.1
 */
public class ReduceAction<T, A> extends BatchAction<T, A> {
	private final Supplier<? extends A>               accumulators;
	private final Function<Tuple2<T, A>, ? extends A> fn;
	private       A                                   acc;

	public ReduceAction(Dispatcher dispatcher, int batchSize, Supplier<? extends A> accumulators, Function<Tuple2<T, A>, ? extends A> fn) {
		super(dispatcher, batchSize, true, false, true);
		this.accumulators = accumulators;
		this.fn = fn;
	}

	@Override
	public void nextCallback(T ev) {
		if (null == acc) {
			acc = (null != accumulators ? accumulators.get() : null);
		}
		acc = fn.apply(Tuple2.of(ev, acc));
	}

	@Override
	protected void flushCallback(T ev) {
		if (acc != null) {
			A _acc = acc;
			acc = null;
			broadcastNext(_acc);
		}
	}

	@Override
	public String toString() {
		return super.toString() + (acc != null ? "{current-reduced=" + acc + "}" : "");
	}
}
