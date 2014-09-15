package uk.ac.sanger.mig.analysis.nodetools.filters;

import java.util.Arrays;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.node.util.ColumnFilter;

public final class NumberColumnFilter implements ColumnFilter {
	
	private final static DataType[] allowed = { IntCell.TYPE, DoubleCell.TYPE };
	
	private final String msg;
	
	public NumberColumnFilter(String message) {
		this.msg = message;
	}

	@Override
	public boolean includeColumn(DataColumnSpec colSpec) {
		return Arrays.asList(allowed).contains(colSpec.getType()) ? true
				: false;
	}

	@Override
	public String allFilteredMsg() {
		return msg;
	}

}
