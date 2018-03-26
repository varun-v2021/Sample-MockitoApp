package com.example.mathapp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.net.URL;

import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

// @RunWith attaches a runner with the test class to initialize the test data
@RunWith(MockitoJUnitRunner.class)
public class MathApplicationTester {

	// @InjectMocks annotation is used to create and inject the mock object
	@InjectMocks
	MathApplication mathApplication = new MathApplication();

	// @Mock annotation is used to create the mock object to be injected
	@Mock
	CalculatorService calcService;

	/* mock API call way of mocking an object instead of using annotation */
	@Before
	public void setUp() {
		mathApplication = new MathApplication();
		calcService = mock(CalculatorService.class);
		mathApplication.setCalculatorService(calcService);
	}

	@Test
	public void testAddAndSubtract() {
		// add the behavior to add numbers
		when(calcService.add(20.0, 10.0)).thenReturn(30.0);
		// subtract the behavior to subtract numbers
		when(calcService.subtract(20.0, 10.0)).thenReturn(10.0);
		// test the subtract functionality
		Assert.assertEquals(mathApplication.subtract(20.0, 10.0), 10.0, 0);
		// test the add functionality
		Assert.assertEquals(mathApplication.add(20.0, 10.0), 30.0, 0);
		// verify call to calcService is made or not
		verify(calcService).add(20.0, 10.0);
		verify(calcService).subtract(20.0, 10.0);
	}

	@Test
	public void testAdd3() {
		System.out.println("In testAdd3 method call");
		// add the behavior to add numbers, verify the output. It will show
		// 'Wanted but not invoked' as when and assertEquals condition are not
		// same
		when(calcService.add(20.0, 15.0)).thenAnswer(new Answer<Double>() {
			@Override
			public Double answer(InvocationOnMock invocation) throws Throwable {
				// get the arguments passed to mock
				Object[] args = invocation.getArguments();
				// get the mock
				Object mock = invocation.getMock();
				// return the result
				return 30.0;
			}
		});
		// test the add functionality
		Assert.assertEquals(mathApplication.add(20.0, 10.0), 30.0, 0);
	}

	@Test
	public void testAddAndSubtract1() {
		// add the behavior to add numbers
		when(calcService.add(20.0, 10.0)).thenReturn(30.0);
		// subtract the behavior to subtract numbers
		when(calcService.subtract(20.0, 10.0)).thenReturn(10.0);
		// then call real method
		when(calcService.subtract(20.0, 10.0)).thenCallRealMethod();		
		// test the add functionality
		Assert.assertEquals(mathApplication.add(20.0, 10.0), 30.0, 0);
		// test the subtract functionality
		Assert.assertEquals(mathApplication.subtract(20.0, 10.0), 10.0, 0);
		// create an inOrder verifier for a single mock
		InOrder inOrder = inOrder(calcService);
		// following will make sure that add is first called then subtract is
		// called.
		inOrder.verify(calcService).subtract(20.0, 10.0);
		inOrder.verify(calcService).add(20.0, 10.0);
	}

	@Test
	public void testAdd() {
		// add the behavior of calc service to add two numbers
		when(calcService.add(10.0, 20.0)).thenReturn(30.00);
		// test the add functionality
		Assert.assertEquals(mathApplication.add(10.0, 20.0), 30.0, 0);
		// Will result in failure as mockito doesn't how to behave if 10.5 is
		// passed as first argument
		// Assert.assertEquals(mathApplication.add(10.5, 20.0),30.0,0);
		// verify the behavior
		verify(calcService).add(20.0, 20.0);
		// try to correct using
		// verify(calcService).add(10.0, 20.0);
	}

	@Test
	public void testAdd1() {
		// add the behavior of calc service to add two numbers
		when(calcService.add(10.0, 20.0)).thenReturn(30.00);
		// add the behavior of calc service to subtract two numbers
		when(calcService.subtract(20.0, 10.0)).thenReturn(10.00);
		// test the add functionality
		Assert.assertEquals(mathApplication.add(10.0, 20.0), 30.0, 0);
		Assert.assertEquals(mathApplication.add(10.0, 20.0), 30.0, 0);
		Assert.assertEquals(mathApplication.add(10.0, 20.0), 30.0, 0);
		// Uncomment and check the result
		// Assert.assertEquals(mathApplication.add(10.0, 20.0),30.0,0);
		// test the subtract functionality
		Assert.assertEquals(mathApplication.subtract(20.0, 10.0), 10.0, 0.0);
		// default call count is 1
		verify(calcService).subtract(20.0, 10.0);
		// check if add function is called three times
		verify(calcService, times(3)).add(10.0, 20.0);
		// verify that method was never called on a mock
		verify(calcService, never()).multiply(10.0, 20.0);
		// check a minimum 1 call count
		verify(calcService, atLeastOnce()).subtract(20.0, 10.0);
		// check if add function is called minimum 2 times
		verify(calcService, atLeast(2)).add(10.0, 20.0);
		// check if add function is called maximum 3 times
		verify(calcService, atMost(3)).add(10.0, 20.0);

		/*
		 * Mockito provides a special Timeout option to test if a method is
		 * called within stipulated time frame.
		 */
		// verify call to add method to be completed within 100 ms
		verify(calcService, timeout(100)).add(20.0, 10.0);

		// invocation count can be added to ensure multiplication invocations
		// can be checked within given timeframe
		verify(calcService, timeout(100).times(1)).subtract(20.0, 10.0);
	}

	@Test
	public void testAddAndSubtract2() {
		// add the behavior to add numbers
		when(calcService.add(20.0, 10.0)).thenReturn(30.0);
		// test the add functionality
		Assert.assertEquals(mathApplication.add(20.0, 10.0), 30.0, 0);
		// reset the mock
		/*
		 * Mockito provides the capability to a reset a mock so that it can be
		 * reused later
		 */
		reset(calcService);
		// test the add functionality after resetting the mock - IMPORTANT,
		// below fails as we have reset the mock
		Assert.assertEquals(mathApplication.add(20.0, 10.0), 30.0, 0);
	}

	@Test
	public void testAddGWTOrBDD() {
		// Given
		given(calcService.add(20.0, 10.0)).willReturn(30.0);
		// when
		double result = calcService.add(20.0, 10.0);
		// then
		Assert.assertEquals(result, 30.0, 0);
	}

	/*
	 * @Test(expected = RuntimeException.class) public void testAdd2(){ //add
	 * the behavior to throw exception doThrow(new
	 * RuntimeException("Add operation not implemented"))
	 * .when(calcService).add(10.0,20.0);
	 * 
	 * //test the add functionality
	 * Assert.assertEquals(mathApplication.add(10.0, 20.0),30.0,0); }
	 */
}