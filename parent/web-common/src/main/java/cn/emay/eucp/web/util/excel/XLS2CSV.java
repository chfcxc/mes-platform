/* ====================================================================                                                          
Licensed to the Apache Software Foundation (ASF) under one or more                                                               
contributor license agreements. See the NOTICE file distributed with                                                             
this work for additional information regarding copyright ownership.                                                              
The ASF licenses this file to You under the Apache License, Version 2.0                                                          
(the "License"); you may not use this file except in compliance with                                                             
the License. You may obtain a copy of the License at                                                                             
                                                                                                                                 
http://www.apache.org/licenses/LICENSE-2.0                                                                                       
                                                                                                                                 
Unless required by applicable law or agreed to in writing, software                                                              
distributed under the License is distributed on an "AS IS" BASIS,                                                                
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.                                                         
See the License for the specific language governing permissions and                                                              
limitations under the License.                                                                                                   
==================================================================== */
package cn.emay.eucp.web.util.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.eventusermodel.EventWorkbookBuilder.SheetRecordCollectingListener;
import org.apache.poi.hssf.eventusermodel.FormatTrackingHSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFEventFactory;
import org.apache.poi.hssf.eventusermodel.HSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFRequest;
import org.apache.poi.hssf.eventusermodel.MissingRecordAwareHSSFListener;
import org.apache.poi.hssf.eventusermodel.dummyrecord.LastCellOfRowDummyRecord;
import org.apache.poi.hssf.eventusermodel.dummyrecord.MissingCellDummyRecord;
import org.apache.poi.hssf.model.HSSFFormulaParser;
import org.apache.poi.hssf.record.BOFRecord;
import org.apache.poi.hssf.record.BlankRecord;
import org.apache.poi.hssf.record.BoolErrRecord;
import org.apache.poi.hssf.record.BoundSheetRecord;
import org.apache.poi.hssf.record.FormulaRecord;
import org.apache.poi.hssf.record.LabelRecord;
import org.apache.poi.hssf.record.LabelSSTRecord;
import org.apache.poi.hssf.record.NoteRecord;
import org.apache.poi.hssf.record.NumberRecord;
import org.apache.poi.hssf.record.RKRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.SSTRecord;
import org.apache.poi.hssf.record.StringRecord;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * A XLS -> CSV processor, that uses the MissingRecordAware EventModel code to ensure it outputs all columns and rows.
 * 
 * @author Nick Burch
 */
@SuppressWarnings("all")
public class XLS2CSV implements HSSFListener {
	private int i = 0;
	private int minColumns;
	private POIFSFileSystem fs;
	private PrintStream output;

	private int lastRowNumber;
	private int lastColumnNumber;

	/** Should we output the formula, or the value it has? */
	private boolean outputFormulaValues = true;

	/** For parsing Formulas */
	private SheetRecordCollectingListener workbookBuildingListener;
	private HSSFWorkbook stubWorkbook;

	// Records we pick up as we process
	private SSTRecord sstRecord;
	private FormatTrackingHSSFListener formatListener;

	/** So we known which sheet we're on */
	private int sheetIndex = -1;
	private BoundSheetRecord[] orderedBSRs;
	@SuppressWarnings("rawtypes")
	private ArrayList boundSheetRecords = new ArrayList();

	// For handling formulas with string results
	private int nextRow;
	private int nextColumn;
	private boolean outputNextStringRecord;

	private final String OUTPUT_CHARSET = "GBK";

	private StringBuffer val = new StringBuffer();
	private List<String[]> stringList = new ArrayList<String[]>();;
	private boolean isOnlyReadFirstRow = false;
	private String separator = "@EXL2901972MAY@";

	public List<String[]> getStringList() {
		return stringList;
	}

	public void setStringList(List<String[]> stringList) {
		this.stringList = stringList;
	}

	public boolean isOnlyReadFirstRow() {
		return isOnlyReadFirstRow;
	}

	public void setOnlyReadFirstRow(boolean isOnlyReadFirstRow) {
		this.isOnlyReadFirstRow = isOnlyReadFirstRow;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	/**
	 * Creates a new XLS -> CSV converter
	 * 
	 * @param fs
	 *            The POIFSFileSystem to process
	 * @param output
	 *            The PrintStream to output the CSV to
	 * @param minColumns
	 *            The minimum number of columns to output, or -1 for no minimum
	 */
	public XLS2CSV(POIFSFileSystem fs, PrintStream output, int minColumns) {
		this.fs = fs;
		this.output = output;
		this.minColumns = minColumns;
	}

	public XLS2CSV(String inputFilePath, String outputFilePath) throws Exception {
		fs = new POIFSFileSystem(new FileInputStream(inputFilePath));
		output = new PrintStream(outputFilePath, OUTPUT_CHARSET);
		minColumns = -1;
	}

	public XLS2CSV(File file) throws Exception {
		fs = new POIFSFileSystem(new FileInputStream(file));
		minColumns = -1;
	}

	public XLS2CSV() {
	}

	/**
	 * Creates a new XLS -> CSV converter
	 * 
	 * @param filename
	 *            The file to process
	 * @param minColumns
	 *            The minimum number of columns to output, or -1 for no minimum
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public XLS2CSV(String filename, int minColumns) throws IOException, FileNotFoundException {
		this(new POIFSFileSystem(new FileInputStream(filename)), System.out, minColumns);
	}

	/**
	 * Initiates the processing of the XLS file to CSV
	 */
	public void process() throws IOException {
		try {
			MissingRecordAwareHSSFListener listener = new MissingRecordAwareHSSFListener(this);
			formatListener = new FormatTrackingHSSFListener(listener);
			HSSFEventFactory factory = new HSSFEventFactory();
			HSSFRequest request = new HSSFRequest();
			if (outputFormulaValues) {
				request.addListenerForAllRecords(formatListener);
			} else {
				workbookBuildingListener = new SheetRecordCollectingListener(formatListener);
				request.addListenerForAllRecords(workbookBuildingListener);
			}
			factory.processWorkbookEvents(request, fs);
		} catch (Exception e) {
			if (e instanceof OnlyReadFirstRowWithXlsException) {
				return;
			}
			throw e;
		} finally {
			if (output != null) {
				output.close();
			}
		}
	}

	/**
	 * Main HSSFListener method, processes events, and outputs the CSV as the file is processed.
	 */
	@Override
	public void processRecord(Record record) throws OfficeXmlFileException {
		// System.out.println("==>" + (i++));
		int thisRow = -1;
		int thisColumn = -1;
		String thisStr = null;

		switch (record.getSid()) {
		case BoundSheetRecord.sid:
			boundSheetRecords.add(record);
			break;
		case BOFRecord.sid:
			BOFRecord br = (BOFRecord) record;
			if (br.getType() == BOFRecord.TYPE_WORKSHEET) {
				// Create sub workbook if required
				if (workbookBuildingListener != null && stubWorkbook == null) {
					stubWorkbook = workbookBuildingListener.getStubHSSFWorkbook();
				}

				// Output the worksheet name
				// Works by ordering the BSRs by the location of
				// their BOFRecords, and then knowing that we
				// process BOFRecords in byte offset order
				sheetIndex++;
				if (orderedBSRs == null) {
					orderedBSRs = BoundSheetRecord.orderByBofPosition(boundSheetRecords);
				}
				// output.println();
				// output.println(orderedBSRs[sheetIndex].getSheetname() + " [" + (sheetIndex + 1) + "]:");
			}
			break;

		case SSTRecord.sid:
			sstRecord = (SSTRecord) record;
			break;

		case BlankRecord.sid:
			BlankRecord brec = (BlankRecord) record;

			thisRow = brec.getRow();
			thisColumn = brec.getColumn();
			thisStr = "";
			break;
		case BoolErrRecord.sid:
			BoolErrRecord berec = (BoolErrRecord) record;

			thisRow = berec.getRow();
			thisColumn = berec.getColumn();
			thisStr = "";
			break;

		case FormulaRecord.sid:
			FormulaRecord frec = (FormulaRecord) record;

			thisRow = frec.getRow();
			thisColumn = frec.getColumn();

			if (outputFormulaValues) {
				if (Double.isNaN(frec.getValue())) {
					// Formula result is a string
					// This is stored in the next record
					outputNextStringRecord = true;
					nextRow = frec.getRow();
					nextColumn = frec.getColumn();
				} else {
					thisStr = formatListener.formatNumberDateCell(frec);
				}
			} else {
				// thisStr = '"' + HSSFFormulaParser.toFormulaString(stubWorkbook, frec.getParsedExpression()) + '"';
				thisStr = HSSFFormulaParser.toFormulaString(stubWorkbook, frec.getParsedExpression());
			}
			break;
		case StringRecord.sid:
			if (outputNextStringRecord) {
				// String for formula
				StringRecord srec = (StringRecord) record;
				thisStr = srec.getString();
				thisRow = nextRow;
				thisColumn = nextColumn;
				outputNextStringRecord = false;
			}
			break;

		case LabelRecord.sid:
			LabelRecord lrec = (LabelRecord) record;

			thisRow = lrec.getRow();
			thisColumn = lrec.getColumn();
			// thisStr = '"' + lrec.getValue() + '"';
			thisStr = lrec.getValue();
			break;
		case LabelSSTRecord.sid:
			LabelSSTRecord lsrec = (LabelSSTRecord) record;

			thisRow = lsrec.getRow();
			thisColumn = lsrec.getColumn();
			if (sstRecord == null) {
				thisStr = '"' + "(No SST Record, can't identify string)" + '"';
			} else {
				// thisStr = '"' + sstRecord.getString(lsrec.getSSTIndex()).toString() + '"';
				thisStr = sstRecord.getString(lsrec.getSSTIndex()).toString();
			}
			break;
		case NoteRecord.sid:
			NoteRecord nrec = (NoteRecord) record;

			thisRow = nrec.getRow();
			thisColumn = nrec.getColumn();
			// TODO: Find object to match nrec.getShapeId()
			thisStr = '"' + "(TODO)" + '"';
			break;
		case NumberRecord.sid:
			NumberRecord numrec = (NumberRecord) record;

			thisRow = numrec.getRow();
			thisColumn = numrec.getColumn();

			// Format
			thisStr = formatListener.formatNumberDateCell(numrec);
			break;
		case RKRecord.sid:
			RKRecord rkrec = (RKRecord) record;

			thisRow = rkrec.getRow();
			thisColumn = rkrec.getColumn();
			thisStr = '"' + "(TODO)" + '"';
			break;
		default:
			break;
		}

		// Handle new row
		if (thisRow != -1 && thisRow != lastRowNumber) {
			lastColumnNumber = -1;
		}

		// Handle missing column
		if (record instanceof MissingCellDummyRecord) {
			MissingCellDummyRecord mc = (MissingCellDummyRecord) record;
			thisRow = mc.getRow();
			thisColumn = mc.getColumn();
			thisStr = "";
		}

		// If we got something to print out, do so
		if (thisStr != null) {
			if (thisColumn > 0) {
				val.append(separator);
				// output.print(',');
			}
			val.append(thisStr);
			// output.print(thisStr);
		}

		// Update column and row count
		if (thisRow > -1)
			lastRowNumber = thisRow;
		if (thisColumn > -1)
			lastColumnNumber = thisColumn;

		// Handle end of row
		if (record instanceof LastCellOfRowDummyRecord) {
			// Print out any missing commas if needed
			if (minColumns > 0) {
				// Columns are 0 based
				if (lastColumnNumber == -1) {
					lastColumnNumber = 0;
				}
				for (int i = lastColumnNumber; i < (minColumns); i++) {
					val.append(separator);
					// output.print(',');
				}
			}
			stringList.add(val.toString().split(separator));
			if (isOnlyReadFirstRow) {
				if (stringList.size() > 0) {
					throw new OnlyReadFirstRowWithXlsException("");
				}
			}
			val = new StringBuffer();
			// We're onto a new row
			lastColumnNumber = -1;
			// End the row
			// output.println();
		}
	}

	public static void main(String[] args) throws Exception {
		XLS2CSV xls2csv = new XLS2CSV(new File("C:/Users/emay/Desktop/1.xls"));
		xls2csv.setOnlyReadFirstRow(true);
		xls2csv.process();
		List<String[]> list = xls2csv.getStringList();
		System.out.println(list.size());
		if (list.size() > 0) {
			for (String[] a : list) {
				for (String s : a) {
					System.out.println(s);
				}
			}
		}
	}
}