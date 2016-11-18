package com.fraz.dartlog;

import android.content.Context;
import android.content.res.Resources;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutChartTest {

    @Mock
    private Context mockContext;
    @Mock
    private Resources mockResource;

    private static final String TEST_DATA = "120,T20 20 D20\n90,T18 D18";

    @Before
    public void setUp() throws Exception {
        InputStream testInput = new ByteArrayInputStream(TEST_DATA.getBytes());
        when(mockContext.getResources()).thenReturn(mockResource);
        when(mockResource.openRawResource(R.raw.double_checkout_chart)).thenReturn(testInput);
    }

    @Test
    @Ignore
    public void testGetCheckoutText() throws Exception {
        CheckoutChart checkoutChart = new CheckoutChart(mockContext, R.raw.double_checkout_chart);
        assertThat(checkoutChart.getCheckoutText(90), equalTo("T18 D18"));
        assertThat(checkoutChart.getCheckoutText(120), equalTo("T20 20 D20"));
        assertThat(checkoutChart.getCheckoutText(121), equalTo("N/A"));
        assertThat(checkoutChart.getCheckoutText(161), equalTo("N/A"));
    }
}