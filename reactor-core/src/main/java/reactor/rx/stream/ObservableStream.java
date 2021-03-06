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
package reactor.rx.stream;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import reactor.event.Event;
import reactor.event.Observable;
import reactor.event.selector.Selector;
import reactor.function.Consumer;

import javax.annotation.Nonnull;

/**
 * Emit signals whenever an Event arrives from the {@link reactor.event.selector.Selector} topic from the {@link
 * reactor.event.Observable}.
 * This stream will never emit a {@link org.reactivestreams.Subscriber#onComplete()}.
 * <p>
 * Create such stream with the provided factory, E.g.:
 * <pre>
 * {@code
 * Streams.on(reactor, $("topic")).consume(System.out::println)
 * }
 * </pre>
 *
 * @author Stephane Maldini
 */
public final class ObservableStream<T> extends PublisherStream<T> {

	public ObservableStream(final @Nonnull Observable observable,
	                        final @Nonnull Selector selector) {

		super(new Publisher<T>() {
			@Override
			public void subscribe(final Subscriber<? super T> subscriber) {
				observable.on(selector, new Consumer<Event<T>>() {
					@Override
					public void accept(Event<T> event) {
						subscriber.onNext(event.getData());
					}
				});
			}
		});
	}
}
