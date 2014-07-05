import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import jxl.CellView;
import jxl.NumberCell;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.write.DateFormat;
import jxl.write.DateTime;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class WriteExcel {

	private WritableCellFormat timesBoldUnderline;
	private WritableCellFormat times;

	public void write(String inputFile, List<ToshlRecord> records)
			throws IOException, WriteException {
		File file = new File(inputFile);
		WorkbookSettings wbSettings = new WorkbookSettings();

		wbSettings.setLocale(new Locale("en", "EN"));

		WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
		workbook.createSheet("Report", 0);
		WritableSheet excelSheet = workbook.getSheet(0);
		createLabel(excelSheet);
		createContent(excelSheet, records);

		workbook.write();
		workbook.close();
	}

	private void createLabel(WritableSheet sheet) throws WriteException {
		// Lets create a times font
		WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
		// Define the cell format
		times = new WritableCellFormat(times10pt);
		// Lets automatically wrap the cells
		times.setWrap(true);

		// create create a bold font with unterlines
		WritableFont times10ptBoldUnderline = new WritableFont(
				WritableFont.TIMES, 10, WritableFont.BOLD, false,
				UnderlineStyle.SINGLE);
		timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
		// Lets automatically wrap the cells
		timesBoldUnderline.setWrap(true);

		CellView cv = new CellView();
		cv.setFormat(times);
		cv.setFormat(timesBoldUnderline);
		cv.setAutosize(true);
	}

	private void createContent(WritableSheet sheet, List<ToshlRecord> records)
			throws WriteException, RowsExceededException {
		boolean header = true;
		for (int i = 0; i < records.size() - 1; i++) {
			if (header)
				addDate(sheet, 1, i, records.get(i).getDate());
			addLabel(sheet, 0, i + 1, records.get(i).getTag());
			addNumber(sheet, 1, i+1, records.get(i).getAmount());

			int skipped = 0;
			for (int y = 0; y < records.size() - 1; y++) {
				if (records.get(i).getTag().equals(records.get(y).getTag())) {
					if (header)
						addDate(sheet, y - skipped + 1, i, records.get(y)
								.getDate());
					// addLabel(sheet, y, i, records.get(y).getTag());
					 addNumber(sheet, y - skipped + 1, i+1, records.get(y).getAmount());
					// records.remove(y);
				} else {
					skipped++;
				}
			}
			header = false;
		}
	}

	private void addLabel(WritableSheet sheet, int column, int row, String s)
			throws WriteException, RowsExceededException {
		Label label;
		label = new Label(column, row, s, times);
		sheet.addCell(label);
	}

	private void addNumber(WritableSheet sheet, int column, int row, String s)
			throws WriteException, RowsExceededException {
		if (s != null) {
			jxl.write.Number label;
			label = new jxl.write.Number(column, row, Double.parseDouble(s
					.replace(",", "")), times);
			sheet.addCell(label);
		}
	}

	private void addDate(WritableSheet sheet, int column, int row, String s)
			throws WriteException, RowsExceededException {
		DateFormat customDateFormat = new DateFormat("yyyy-MM");
		WritableCellFormat dateFormat = new WritableCellFormat(customDateFormat);
		DateTime dateCell = null;
		try {
			dateCell = new DateTime(column, row, new SimpleDateFormat(
					"yyyy-MM-dd", Locale.ENGLISH).parse(s), dateFormat);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		sheet.addCell(dateCell);
	}
}