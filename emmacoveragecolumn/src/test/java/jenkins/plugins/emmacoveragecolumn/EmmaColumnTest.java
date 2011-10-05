package jenkins.plugins.emmacoveragecolumn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import hudson.model.Job;

import java.math.BigDecimal;

import jenkins.plugins.emmacoveragecolumn.EmmaColumn;

import org.easymock.classextension.EasyMock;
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
	public void testGetFillColor() throws Exception {
		assertEquals("008B00", emmaColumn.getFillColor(BigDecimal.valueOf(100)));

	}
}
