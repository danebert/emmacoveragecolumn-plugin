package jenkins.plugins.emmacoveragecolumn;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Job;
import hudson.model.Run;
import hudson.plugins.emma.EmmaBuildAction;
import hudson.plugins.emma.Ratio;
import hudson.plugins.emma.CoverageReport;
import hudson.views.ListViewColumn;

import java.awt.Color;
import java.math.BigDecimal;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

/**
 * View column that shows the code coverage (line) percentage
 * 
 */
public class EmmaColumn extends ListViewColumn {

	@DataBoundConstructor
	public EmmaColumn() {
	}

	public String getPercent(final Job<?, ?> job) {
		final Run<?, ?> lastSuccessfulBuild = job.getLastSuccessfulBuild();
		final StringBuilder stringBuilder = new StringBuilder();

		if (lastSuccessfulBuild == null
				|| lastSuccessfulBuild.getAction(EmmaBuildAction.class) == null) {
			stringBuilder.append("N/A");
		} else {
			final Double percent = getLinePercent(lastSuccessfulBuild);
			stringBuilder.append(percent);
		}

		return stringBuilder.toString();
	}

	public String getLineColor(final BigDecimal amount) {
		if (amount == null) {
			return null;
		}
		return CoverageRange.valueOf(amount.doubleValue()).getLineHexString();
	}

	public String getFillColor(final BigDecimal amount) {
		if (amount == null) {
			return null;
		}
		final Color c = CoverageRange.fillColorOf(amount.doubleValue());
		return CoverageRange.colorAsHexString(c);
	}

	public BigDecimal getLineCoverage(final Job<?, ?> job) {
		final Run<?, ?> lastSuccessfulBuild = job.getLastSuccessfulBuild();
		return BigDecimal.valueOf(getLinePercent(lastSuccessfulBuild)
				.doubleValue());
	}

	private Double getLinePercent(final Run<?, ?> lastSuccessfulBuild) {
		final EmmaBuildAction action = lastSuccessfulBuild
				.getAction(EmmaBuildAction.class);
		final Float percentageFloat = getPercentageFloat(action);
		final double doubleValue = percentageFloat.doubleValue();

		final int decimalPlaces = 2;
		BigDecimal bigDecimal = new BigDecimal(doubleValue);

		// setScale is immutable
		bigDecimal = bigDecimal.setScale(decimalPlaces,
				BigDecimal.ROUND_HALF_UP);
		return bigDecimal.doubleValue();
	}

	private Float getPercentageFloat(final EmmaBuildAction action) {

		final CoverageReport result = action.getResult();

		final Ratio ratio = result.getLineCoverage();

		return ratio.getPercentageFloat();
	}

	@Extension
	public static final Descriptor<ListViewColumn> DESCRIPTOR = new DescriptorImpl();

	@Override
	public Descriptor<ListViewColumn> getDescriptor() {
		return DESCRIPTOR;
	}

	private static class DescriptorImpl extends Descriptor<ListViewColumn> {
		@Override
		public ListViewColumn newInstance(final StaplerRequest req,
				final JSONObject formData) throws FormException {
			return new EmmaColumn();
		}

		@Override
		public String getDisplayName() {
			return "Line Coverage";
		}
	}
}