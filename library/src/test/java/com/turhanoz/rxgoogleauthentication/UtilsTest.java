package com.turhanoz.rxgoogleauthentication;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class UtilsTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldThrowExcetionOnCheckArguments() throws Exception {
        String nullString = null;
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("You need to set a non null String");

        Utils.checkArgumentNotNull(nullString, "String");
    }
}