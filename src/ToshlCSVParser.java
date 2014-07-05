import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

import jxl.write.WriteException;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.dozer.CsvDozerBeanReader;
import org.supercsv.prefs.CsvPreference;

public class ToshlCSVParser extends JPanel {
	private static CellProcessor[] getProcessors() {

		final CellProcessor[] processors = new CellProcessor[] {
				new Optional(), new Optional(), new Optional(), new Optional(),
				new Optional(), new Optional() };

		return processors;
	}

	private static final String[] FIELD_MAPPING = new String[] { "Date", "Tag",
			"Amount", "Amount2", "Currency", "Description" };

	public void ReadCSV(File file) {
		CsvDozerBeanReader beanReader = null;
		try {
			beanReader = new CsvDozerBeanReader(new FileReader(
					file.getAbsoluteFile()), new CsvPreference.Builder(
					CsvPreference.STANDARD_PREFERENCE)
					.surroundingSpacesNeedQuotes(true).build());

			beanReader.configureBeanMapping(ToshlRecord.class, FIELD_MAPPING);
			final CellProcessor[] processors = getProcessors();

			boolean accumulated;
			boolean isFirstLine = true;
			ToshlRecord record = null;
			List<ToshlRecord> records = new ArrayList<ToshlRecord>();
			while ((record = beanReader.read(ToshlRecord.class, processors)) != null) {
				if (isFirstLine) {
					isFirstLine = false;
					continue;
				}

				System.out.println(record.toString());
				accumulated = false;
				for (ToshlRecord aRecord : records) {
					int year1 = new SimpleDateFormat("yyyy-MM-dd",
							Locale.ENGLISH).parse(aRecord.getDate()).getYear();
					int month1 = new SimpleDateFormat("yyyy-MM-dd",
							Locale.ENGLISH).parse(aRecord.getDate()).getMonth();
					int year2 = new SimpleDateFormat("yyyy-MM-dd",
							Locale.ENGLISH).parse(record.getDate()).getYear();
					int month2 = new SimpleDateFormat("yyyy-MM-dd",
							Locale.ENGLISH).parse(record.getDate()).getMonth();

					if (record != null && aRecord != null)
						if ((record.getTag().equals(aRecord.getTag()))
								&& (year1 == year2) && (month1 == month2)
								&& record.getAmount() != null
								&& aRecord.getAmount() != null) {
							aRecord.setAmount(String.valueOf(Double
									.parseDouble(record.getAmount().replace(
											",", ""))
									+ Double.parseDouble(aRecord.getAmount()
											.replace(",", ""))));
							accumulated = true;
							break;
						}
				}

				if (!accumulated)
					records.add(record);

				new WriteExcel().write(file.getParent() + "/test.xls", records);
			}

			System.out.println(records.size());

			System.out.println("----------");
			for (ToshlRecord bRecord : records) {
				System.out.println(bRecord.toString());
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (beanReader != null) {
				try {
					beanReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public ToshlCSVParser() {
		JFileChooser fc = null;
		fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(ToshlCSVParser.this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			ReadCSV(file);
		}
	}

	public static void main(String[] args) {
		new ToshlCSVParser();
	}
}
