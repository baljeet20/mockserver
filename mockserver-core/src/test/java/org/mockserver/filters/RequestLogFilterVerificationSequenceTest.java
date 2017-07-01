package org.mockserver.filters;

import org.junit.Test;
import org.mockserver.verify.VerificationSequence;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockserver.character.Character.NEW_LINE;
import static org.mockserver.model.HttpRequest.request;

/**
 * @author jamesdbloom
 */
public class RequestLogFilterVerificationSequenceTest {

    @Test
    public void shouldPassVerificationWithNullRequest() {
        // given
        RequestLogFilter requestLogFilter = new RequestLogFilter();

        // when
        requestLogFilter.onRequest(request("one"));
        requestLogFilter.onRequest(request("multi"));
        requestLogFilter.onRequest(request("three"));
        requestLogFilter.onRequest(request("multi"));
        requestLogFilter.onRequest(request("four"));

        // then
        assertThat(requestLogFilter.verify((VerificationSequence) null), is(""));
    }

    @Test
    public void shouldPassVerificationSequenceWithNoRequest() {
        // given
        RequestLogFilter requestLogFilter = new RequestLogFilter();

        // when
        requestLogFilter.onRequest(request("one"));
        requestLogFilter.onRequest(request("multi"));
        requestLogFilter.onRequest(request("three"));
        requestLogFilter.onRequest(request("multi"));
        requestLogFilter.onRequest(request("four"));

        // then
        assertThat(requestLogFilter.verify(
                        new VerificationSequence()
                                .withRequests(

                                )
                ),
                is(""));
    }

    @Test
    public void shouldPassVerificationSequenceWithOneRequest() {
        // given
        RequestLogFilter requestLogFilter = new RequestLogFilter();

        // when
        requestLogFilter.onRequest(request("one"));
        requestLogFilter.onRequest(request("multi"));
        requestLogFilter.onRequest(request("three"));
        requestLogFilter.onRequest(request("multi"));
        requestLogFilter.onRequest(request("four"));

        // then
        assertThat(requestLogFilter.verify(
                        new VerificationSequence()
                                .withRequests(
                                        request("one")
                                )
                ),
                is(""));
        assertThat(requestLogFilter.verify(
                        new VerificationSequence()
                                .withRequests(
                                        request("multi")
                                )
                ),
                is(""));
        assertThat(requestLogFilter.verify(
                        new VerificationSequence()
                                .withRequests(
                                        request("three")
                                )
                ),
                is(""));
        assertThat(requestLogFilter.verify(
                        new VerificationSequence()
                                .withRequests(
                                        request("four")
                                )
                ),
                is(""));
    }

    @Test
    public void shouldPassVerificationSequenceWithTwoRequests() {
        // given
        RequestLogFilter requestLogFilter = new RequestLogFilter();

        // when
        requestLogFilter.onRequest(request("one"));
        requestLogFilter.onRequest(request("multi"));
        requestLogFilter.onRequest(request("three"));
        requestLogFilter.onRequest(request("multi"));
        requestLogFilter.onRequest(request("four"));

        // then - next to each other
        assertThat(requestLogFilter.verify(
                        new VerificationSequence()
                                .withRequests(
                                        request("one"),
                                        request("multi")
                                )
                ),
                is(""));
        assertThat(requestLogFilter.verify(
                        new VerificationSequence()
                                .withRequests(
                                        request("multi"),
                                        request("three")
                                )
                ),
                is(""));
        assertThat(requestLogFilter.verify(
                        new VerificationSequence()
                                .withRequests(
                                        request("three"),
                                        request("multi")
                                )
                ),
                is(""));
        assertThat(requestLogFilter.verify(
                        new VerificationSequence()
                                .withRequests(
                                        request("multi"),
                                        request("four")
                                )
                ),
                is(""));
        // then - not next to each other
        assertThat(requestLogFilter.verify(
                        new VerificationSequence()
                                .withRequests(
                                        request("one"),
                                        request("three")
                                )
                ),
                is(""));
        assertThat(requestLogFilter.verify(
                        new VerificationSequence()
                                .withRequests(
                                        request("one"),
                                        request("four")
                                )
                ),
                is(""));
        assertThat(requestLogFilter.verify(
                        new VerificationSequence()
                                .withRequests(
                                        request("multi"),
                                        request("multi")
                                )
                ),
                is(""));
        assertThat(requestLogFilter.verify(
                        new VerificationSequence()
                                .withRequests(
                                        request("three"),
                                        request("four")
                                )
                ),
                is(""));
    }

    @Test
    public void shouldFailVerificationSequenceWithOneRequest() {
        // given
        RequestLogFilter requestLogFilter = new RequestLogFilter();

        // when
        requestLogFilter.onRequest(request("one"));
        requestLogFilter.onRequest(request("multi"));
        requestLogFilter.onRequest(request("three"));
        requestLogFilter.onRequest(request("multi"));
        requestLogFilter.onRequest(request("four"));

        // then
        assertThat(requestLogFilter.verify(
                        new VerificationSequence()
                                .withRequests(
                                        request("five")
                                )
                ),
                is("Request sequence not found, expected:<[ {" + NEW_LINE +
                        "  \"path\" : \"five\"" + NEW_LINE +
                        "} ]> but was:<[ {" + NEW_LINE +
                        "  \"path\" : \"one\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"three\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"four\"" + NEW_LINE +
                        "} ]>"));
    }

    @Test
    public void shouldFailVerificationSequenceWithTwoRequestsWrongOrder() {
        // given
        RequestLogFilter requestLogFilter = new RequestLogFilter();

        // when
        requestLogFilter.onRequest(request("one"));
        requestLogFilter.onRequest(request("multi"));
        requestLogFilter.onRequest(request("three"));
        requestLogFilter.onRequest(request("multi"));
        requestLogFilter.onRequest(request("four"));

        // then - next to each other
        assertThat(requestLogFilter.verify(
                        new VerificationSequence()
                                .withRequests(
                                        request("multi"),
                                        request("one")
                                )
                ),
                is("Request sequence not found, expected:<[ {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"one\"" + NEW_LINE +
                        "} ]> but was:<[ {" + NEW_LINE +
                        "  \"path\" : \"one\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"three\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"four\"" + NEW_LINE +
                        "} ]>"));
        assertThat(requestLogFilter.verify(
                        new VerificationSequence()
                                .withRequests(
                                        request("four"),
                                        request("multi")
                                )
                ),
                is("Request sequence not found, expected:<[ {" + NEW_LINE +
                        "  \"path\" : \"four\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "} ]> but was:<[ {" + NEW_LINE +
                        "  \"path\" : \"one\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"three\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"four\"" + NEW_LINE +
                        "} ]>"));
        // then - not next to each other
        assertThat(requestLogFilter.verify(
                        new VerificationSequence()
                                .withRequests(
                                        request("three"),
                                        request("one")
                                )
                ),
                is("Request sequence not found, expected:<[ {" + NEW_LINE +
                        "  \"path\" : \"three\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"one\"" + NEW_LINE +
                        "} ]> but was:<[ {" + NEW_LINE +
                        "  \"path\" : \"one\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"three\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"four\"" + NEW_LINE +
                        "} ]>"));
        assertThat(requestLogFilter.verify(
                        new VerificationSequence()
                                .withRequests(
                                        request("four"),
                                        request("one")
                                )
                ),
                is("Request sequence not found, expected:<[ {" + NEW_LINE +
                        "  \"path\" : \"four\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"one\"" + NEW_LINE +
                        "} ]> but was:<[ {" + NEW_LINE +
                        "  \"path\" : \"one\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"three\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"four\"" + NEW_LINE +
                        "} ]>"));
        assertThat(requestLogFilter.verify(
                        new VerificationSequence()
                                .withRequests(
                                        request("four"),
                                        request("three")
                                )
                ),
                is("Request sequence not found, expected:<[ {" + NEW_LINE +
                        "  \"path\" : \"four\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"three\"" + NEW_LINE +
                        "} ]> but was:<[ {" + NEW_LINE +
                        "  \"path\" : \"one\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"three\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"four\"" + NEW_LINE +
                        "} ]>"));
    }

    @Test
    public void shouldFailVerificationSequenceWithTwoRequestsFirstIncorrect() {
        // given
        RequestLogFilter requestLogFilter = new RequestLogFilter();

        // when
        requestLogFilter.onRequest(request("one"));
        requestLogFilter.onRequest(request("multi"));
        requestLogFilter.onRequest(request("three"));
        requestLogFilter.onRequest(request("multi"));
        requestLogFilter.onRequest(request("four"));

        // then - next to each other
        assertThat(requestLogFilter.verify(
                        new VerificationSequence()
                                .withRequests(
                                        request("zero"),
                                        request("multi")
                                )
                ),
                is("Request sequence not found, expected:<[ {" + NEW_LINE +
                        "  \"path\" : \"zero\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "} ]> but was:<[ {" + NEW_LINE +
                        "  \"path\" : \"one\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"three\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"four\"" + NEW_LINE +
                        "} ]>"));
        assertThat(requestLogFilter.verify(
                        new VerificationSequence()
                                .withRequests(
                                        request("zero"),
                                        request("three")
                                )
                ),
                is("Request sequence not found, expected:<[ {" + NEW_LINE +
                        "  \"path\" : \"zero\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"three\"" + NEW_LINE +
                        "} ]> but was:<[ {" + NEW_LINE +
                        "  \"path\" : \"one\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"three\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"four\"" + NEW_LINE +
                        "} ]>"));
        assertThat(requestLogFilter.verify(
                        new VerificationSequence()
                                .withRequests(
                                        request("zero"),
                                        request("four")
                                )
                ),
                is("Request sequence not found, expected:<[ {" + NEW_LINE +
                        "  \"path\" : \"zero\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"four\"" + NEW_LINE +
                        "} ]> but was:<[ {" + NEW_LINE +
                        "  \"path\" : \"one\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"three\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"four\"" + NEW_LINE +
                        "} ]>"));
    }

    @Test
    public void shouldFailVerificationSequenceWithTwoRequestsSecondIncorrect() {
        // given
        RequestLogFilter requestLogFilter = new RequestLogFilter();

        // when
        requestLogFilter.onRequest(request("one"));
        requestLogFilter.onRequest(request("multi"));
        requestLogFilter.onRequest(request("three"));
        requestLogFilter.onRequest(request("multi"));
        requestLogFilter.onRequest(request("four"));

        // then - next to each other
        assertThat(requestLogFilter.verify(
                        new VerificationSequence()
                                .withRequests(
                                        request("one"),
                                        request("five")
                                )
                ),
                is("Request sequence not found, expected:<[ {" + NEW_LINE +
                        "  \"path\" : \"one\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"five\"" + NEW_LINE +
                        "} ]> but was:<[ {" + NEW_LINE +
                        "  \"path\" : \"one\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"three\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"four\"" + NEW_LINE +
                        "} ]>"));
        assertThat(requestLogFilter.verify(
                        new VerificationSequence()
                                .withRequests(
                                        request("multi"),
                                        request("five")
                                )
                ),
                is("Request sequence not found, expected:<[ {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"five\"" + NEW_LINE +
                        "} ]> but was:<[ {" + NEW_LINE +
                        "  \"path\" : \"one\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"three\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"four\"" + NEW_LINE +
                        "} ]>"));
        assertThat(requestLogFilter.verify(
                        new VerificationSequence()
                                .withRequests(
                                        request("three"),
                                        request("five")
                                )
                ),
                is("Request sequence not found, expected:<[ {" + NEW_LINE +
                        "  \"path\" : \"three\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"five\"" + NEW_LINE +
                        "} ]> but was:<[ {" + NEW_LINE +
                        "  \"path\" : \"one\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"three\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"four\"" + NEW_LINE +
                        "} ]>"));
    }

    @Test
    public void shouldFailVerificationSequenceWithThreeRequestsWrongOrder() {
        // given
        RequestLogFilter requestLogFilter = new RequestLogFilter();

        // when
        requestLogFilter.onRequest(request("one"));
        requestLogFilter.onRequest(request("multi"));
        requestLogFilter.onRequest(request("three"));
        requestLogFilter.onRequest(request("multi"));
        requestLogFilter.onRequest(request("four"));

        // then - next to each other
        assertThat(requestLogFilter.verify(
                        new VerificationSequence()
                                .withRequests(
                                        request("one"),
                                        request("four"),
                                        request("multi")
                                )
                ),
                is("Request sequence not found, expected:<[ {" + NEW_LINE +
                        "  \"path\" : \"one\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"four\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "} ]> but was:<[ {" + NEW_LINE +
                        "  \"path\" : \"one\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"three\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"four\"" + NEW_LINE +
                        "} ]>"));
        assertThat(requestLogFilter.verify(
                        new VerificationSequence()
                                .withRequests(
                                        request("one"),
                                        request("multi"),
                                        request("one")
                                )
                ),
                is("Request sequence not found, expected:<[ {" + NEW_LINE +
                        "  \"path\" : \"one\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"one\"" + NEW_LINE +
                        "} ]> but was:<[ {" + NEW_LINE +
                        "  \"path\" : \"one\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"three\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"four\"" + NEW_LINE +
                        "} ]>"));
        // then - not next to each other
        assertThat(requestLogFilter.verify(
                        new VerificationSequence()
                                .withRequests(
                                        request("four"),
                                        request("one"),
                                        request("multi")
                                )
                ),
                is("Request sequence not found, expected:<[ {" + NEW_LINE +
                        "  \"path\" : \"four\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"one\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "} ]> but was:<[ {" + NEW_LINE +
                        "  \"path\" : \"one\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"three\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"four\"" + NEW_LINE +
                        "} ]>"));
        assertThat(requestLogFilter.verify(
                        new VerificationSequence()
                                .withRequests(
                                        request("multi"),
                                        request("three"),
                                        request("one")
                                )
                ),
                is("Request sequence not found, expected:<[ {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"three\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"one\"" + NEW_LINE +
                        "} ]> but was:<[ {" + NEW_LINE +
                        "  \"path\" : \"one\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"three\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"four\"" + NEW_LINE +
                        "} ]>"));

    }

    @Test
    public void shouldFailVerificationSequenceWithThreeRequestsDuplicateMissing() {
        // given
        RequestLogFilter requestLogFilter = new RequestLogFilter();

        // when
        requestLogFilter.onRequest(request("one"));
        requestLogFilter.onRequest(request("multi"));
        requestLogFilter.onRequest(request("three"));
        requestLogFilter.onRequest(request("multi"));
        requestLogFilter.onRequest(request("four"));

        // then
        assertThat(requestLogFilter.verify(
                        new VerificationSequence()
                                .withRequests(
                                        request("multi"),
                                        request("multi"),
                                        request("multi")
                                )
                ),
                is("Request sequence not found, expected:<[ {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "} ]> but was:<[ {" + NEW_LINE +
                        "  \"path\" : \"one\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"three\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"multi\"" + NEW_LINE +
                        "}, {" + NEW_LINE +
                        "  \"path\" : \"four\"" + NEW_LINE +
                        "} ]>"));
    }

}
