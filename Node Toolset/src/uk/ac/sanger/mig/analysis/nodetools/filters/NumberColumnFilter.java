package uk.ac.sanger.mig.analysis.nodetools.filters;

import java.util.Arrays;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.node.util.ColumnFilter;

public final class NumberColumnFilter implements ColumnFilter {
	
	private DataType[] allowed = { IntCell.TYPE, DoubleCell.TYPE };

	@Override
	public boolean includeColumn(DataColumnSpec colSpec) {
		return Arrays.asList(allowed).contains(colSpec.getType()) ? true
				: false;
	}

	@Override
	public String allFilteredMsg() {
		return "No Boundary Columns?";
	}

}
