/*
 * Copyright (c) 2011-2016 Pivotal Software Inc, All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package reactor.core.publisher;

import org.junit.Test;
import reactor.test.subscriber.AssertSubscriber;

public class MonoOtherwiseIfEmptyTest {

	@Test(expected = NullPointerException.class)
	public void sourceNull() {
		new MonoOtherwiseIfEmpty<>(null, Mono.never());
	}

	@Test(expected = NullPointerException.class)
	public void otherNull() {
		Mono.never()
		    .otherwiseIfEmpty(null);
	}

	@Test
	public void nonEmpty() {
		AssertSubscriber<Integer> ts = AssertSubscriber.create();

		Mono.just(1)
		    .otherwiseIfEmpty(Mono.just(10))
		    .subscribe(ts);

		ts.assertValues(1)
		  .assertComplete()
		  .assertNoError();
	}

	@Test
	public void nonEmptyBackpressured() {
		AssertSubscriber<Integer> ts = AssertSubscriber.create(0);

		Mono.just(1)
		    .otherwiseIfEmpty(Mono.just(10))
		    .subscribe(ts);

		ts.assertNoValues()
		  .assertNoError()
		  .assertNotComplete();

		ts.request(2);

		ts.assertValues(1)
		  .assertComplete()
		  .assertNoError();
	}

	@Test
	public void empty() {
		AssertSubscriber<Integer> ts = AssertSubscriber.create();

		Mono.<Integer>empty()
		    .otherwiseIfEmpty(Mono.just(10))
		    .subscribe(ts);

		ts.assertValues(10)
		  .assertComplete()
		  .assertNoError();
	}

	@Test
	public void emptyBackpressured() {
		AssertSubscriber<Integer> ts = AssertSubscriber.create(0);

		Mono.<Integer>empty()
		    .otherwiseIfEmpty(Mono.just(10))
		    .subscribe(ts);

		ts.assertNoValues()
		  .assertNoError()
		  .assertNotComplete();

		ts.request(2);

		ts.assertValues(10)
		  .assertComplete()
		  .assertNoError();
	}

}
