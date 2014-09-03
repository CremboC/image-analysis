package uk.ac.sanger.mig.analysis.nodetools.filters;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.node.util.ColumnFilter;
import org.knime.knip.base.data.img.ImgPlusCell;

public final class ImgPlusColumnFilter implements ColumnFilter {

	@Override
	public boolean includeColumn(DataColumnSpec colSpec) {
		return colSpec.getType() == ImgPlusCell.TYPE ? true
				: false;
	}

	@Override
	public String allFilteredMsg() {
		return "Missing Image Column";
	}

}
