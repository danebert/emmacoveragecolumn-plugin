package jenkins.plugins.emmacoveragecolumn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import hudson.model.Build;
import hudson.model.Job;
import hudson.plugins.emma.EmmaBuildAction;

import java.math.BigDecimal;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

public class EmmaColumnTest {

	protected Float percentFloat;
	private EmmaColumn emmaColumn;

	@Before
	public void setUp() {
		emmaColumn = new EmmaColumn();
	}

	@Test
	public void testGetPercentWithNoLastSuccessfulBuild() {
		final Job<?, ?> mockJob = EasyMock.createMock(Job.class);
		EasyMock.expect(mockJob.getLastSuccessfulBuild()).andReturn(null);
		EasyMock.replay(mockJob);
		assertEquals("N/A", emmaColumn.getPercent(mockJob));
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testGetPercentWithNoAction() {
		final Job mockJob = EasyMock.createMock(Job.class);
		final Build mockLlastSuccessful = EasyMock.createMock(Build.class);

		EasyMock.expect(mockJob.getLastSuccessfulBuild()).andReturn(
				mockLlastSuccessful);

		EasyMock.expect(mockLlastSuccessful.getAction(EmmaBuildAction.class))
				.andReturn(null);

		EasyMock.replay(mockJob, mockLlastSuccessful);
		assertEquals("N/A", emmaColumn.getPercent(mockJob));
	}

	@Test
	public void testGetLineColorWithNull() throws Exception {
		assertNull(emmaColumn.getLineColor(null));
	}

	@Test
	public void testGetLineColor() throws Exception {
		assertEquals("EEEEEE", emmaColumn.getLineColor(BigDecimal.valueOf(100)));

	}

	@Test
	public void testGetFillColorWithNull() throws Exception {
		assertNull(emmaColumn.getFillColor(null));
	}

	@Test
	public void testGetFillColor100() throws Exception {
		assertEquals("008B00", emmaColumn.getFillColor(BigDecimal.valueOf(100)));
	}
}
