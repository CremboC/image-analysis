package uk.ac.sanger.mig.analysis.nodetools.filters;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.def.IntCell;
import org.knime.core.node.util.ColumnFilter;

/**
 * Column filter that only leaves integer columns
 * @author Paulius pi1@sanger.ac.uk
 *
 */
public final class IntColumnFilter implements ColumnFilter {

	@Override
	public boolean includeColumn(DataColumnSpec colSpec) {
		return colSpec.getType() == IntCell.TYPE ? true
				: false;
	}

	@Override
	public String allFilteredMsg() {
		return "No Boundary Columns?";
	}

}
