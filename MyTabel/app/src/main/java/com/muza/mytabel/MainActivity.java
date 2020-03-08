
package com.muza.mytabel;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.widget.TableLayout.*;
import android.view.*;
import android.text.*;
import android.view.inputmethod.*;
import android.content.*;
import java.io.*;
import android.util.*;
import java.util.*;
import java.math.*;
import android.graphics.pdf.*;
import android.graphics.*;

public class MainActivity extends  Activity implements OnClickListener
{
	final String LOG_TAG = "myLogs";

	final String FILENAME = "file";

	final String DIR_SD = "MyFiles";
	final String FILENAME_SD = "fileSD";

	private int ROW = 0;

	private TableLayout tblLayout = null;
	private TableRow tableRow = null;
	
	private Button btnSet;
	private Button btnClear;
	private Button btnAddRow;
	private Button btnDellRow;
	
	private EditText editText1;
	private TextView tvInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		tblLayout = findViewById(R.id.tableLayout);

		btnSet = findViewById(R.id.mainButtonSet);
		btnSet.setOnClickListener(this);
		btnClear = findViewById(R.id.mainButtonClear);
		btnClear.setOnClickListener(this);
		btnAddRow = findViewById(R.id.mainAddRow);
		btnAddRow.setOnClickListener(this);
		btnDellRow = findViewById(R.id.mainDellRow);
		btnDellRow.setOnClickListener(this);
		
		editText1 = findViewById(R.id.mainEditText1);
		tvInfo = findViewById(R.id.mainTextView1);
		editText1.addTextChangedListener(new TextWatcher(){
				@Override
				public void afterTextChanged(Editable s) {
					// Прописываем то, что надо выполнить после изменения текста
					//setTable();
					
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				
					
				}

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					
				}
			});

		editText1.setOnKeyListener(new OnKeyListener() {
				public boolean onKey(View view, int keyCode, KeyEvent event) {
				   if ((event.getAction() == KeyEvent.ACTION_UP) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
						
						addRow();
					    editText1.setText("");
					    //InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					    //imm.showSoftInput(editText1, InputMethodManager.SHOW_FORCED);
					    //editText1.requestFocus();
						
						return true;
					} else {
						return false;
					}
				}
			});
		editText1.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_TEXT);
    }

	@Override
	protected void onResume()
	{
		readTable();
		super.onResume();
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId()) {
			case R.id.mainButtonSet:
				readTable();
				//setTable();
				break;
				
			case R.id.mainButtonClear:
				writeTable();
				break;
				
			case R.id.mainAddRow:
				addRow();
				createPdf("test");
				break;
				
			case R.id.mainDellRow:
				delRow();
				
				break;
		}

	}
	
	void addRow(){
		
		String tmp;
		String sep = ".";
		String str = editText1.getText().toString();
		String[] s1 = str.split(" ");
		if(s1.length !=2){
			return;
		}
		float x = Float.parseFloat(s1[0]);
		float y = Float.parseFloat(s1[1]);

		int h = (int)x;
		tmp   = Float.toString(x).split("\\.")[1];
		int m = Integer.parseInt(tmp);
		if(tmp.length() == 1)
			m = Integer.parseInt(tmp)*10;
		//tvInfo.append(tmp + " " + m + " " + tmp.length());

		int h1 = (int)y;
		tmp   = Float.toString(y).split("\\.")[1];
		int m1 = Integer.parseInt(tmp);
		if(tmp.length() == 1)
			m1 = Integer.parseInt(tmp)*10;

		int hm  = h* 60+m;
		int hm1 = h1*60+m1;
		int res = hm1-hm;
		int hour = res/60;
		int min  = res-hour*60;
		
		
		LayoutInflater inflater = LayoutInflater.from(this);
		//Создаем строку таблицы, используя шаблон из файла /res/layout/table_row.xml
		TableRow tr = (TableRow) inflater.inflate(R.layout.row, null);
		//Находим ячейку для номера дня по идентификатору
		TextView tv1 =  tr.findViewById(R.id.col1);
		TextView tv2 =  tr.findViewById(R.id.col2);
		TextView tv3 =  tr.findViewById(R.id.col3);
		TextView tv4 =  tr.findViewById(R.id.col4);
		
		tv2.setText(s1[0]);
		tv3.setText(s1[1]);
		tv4.setText(Integer.toString(hour)+sep+Integer.toString(min));
		
		tv1.setText( Integer.toString(tblLayout.getChildCount()+1) );
		
		tblLayout.addView(tr);
	}
	
	void delRow(){
		int i = tblLayout.getChildCount()-1;
		if (i>=0)
			tblLayout.removeViewAt(i);
	}
	
	void readTable() {
		try {
			
			int tMin =0;
		
			// открываем поток для чтения
			BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput(FILENAME_SD)));
			
			tblLayout.removeAllViews();
			
			String sep = ".";
			String str = "";
			String tmp = "";
			// читаем содержимое
			while ((str = br.readLine()) != null) {
				String s1[] = str.split(" ");
				//Scanner sc = new Scanner(s1[0]);//.useDelimiter("\\s* \\s*");
				//float n = sc.nextFloat();
				//sc.close();
				float x = Float.parseFloat(s1[0]);
				float y = Float.parseFloat(s1[1]);
				
				int h = (int)x;
				tmp   = Float.toString(x).split("\\.")[1];
				int m = Integer.parseInt(tmp);
				if(tmp.length() == 1)
					m = Integer.parseInt(tmp)*10;
				//tvInfo.append(tmp + " " + m + " " + tmp.length());
				
				int h1 = (int)y;
				tmp   = Float.toString(y).split("\\.")[1];
				int m1 = Integer.parseInt(tmp);
				if(tmp.length() == 1)
					m1 = Integer.parseInt(tmp)*10;
					
				int hm  = h* 60+m;
				int hm1 = h1*60+m1;
				int res = hm1-hm;
				int hour = res/60;
				int min  = res-hour*60;
			    tMin += res;
				
				LayoutInflater inflater = LayoutInflater.from(this);
				//Создаем строку таблицы, используя шаблон из файла /res/layout/table_row.xml
				TableRow tr = (TableRow) inflater.inflate(R.layout.row, null);
				//Находим ячейку для номера дня по идентификатору
				TextView tv1 =  tr.findViewById(R.id.col1);
				TextView tv2 =  tr.findViewById(R.id.col2);
				TextView tv3 =  tr.findViewById(R.id.col3);
				TextView tv4 =  tr.findViewById(R.id.col4);

				tv2.setText(s1[0]);
				tv3.setText(s1[1]);
				tv4.setText(Integer.toString(hour)+sep+Integer.toString(min));

				tv1.setText( Integer.toString(tblLayout.getChildCount()+1) );
				tv1.setText( Integer.toString(tblLayout.getChildCount()+1) );

				tblLayout.addView(tr);
			}
			int thour = tMin/60;
			int tmin  = tMin-thour*60;
			tvInfo.append(""+thour+":"+tmin+" ");		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	TextView setTextView(){
		TextView tv = new TextView(this);
		tv.setBackgroundResource(R.drawable.edit_text_back);
		tv.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		return tv;
	}

	void writeTable(){
		
		try {
			int rowcount = tblLayout.getChildCount();
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(openFileOutput(FILENAME_SD, MODE_PRIVATE)));
			
			for (int row = 0; row < rowcount; row++){
				TableRow tbr = (TableRow) tblLayout.getChildAt(row);
				
				TextView tv = (TextView) tbr.getChildAt(1);
				String res1 = tv.getText().toString();
			    		 tv = (TextView) tbr.getChildAt(2);
				String res2 = tv.getText().toString();
				bw.write(res1+" "+res2);	
				if(row != rowcount-1) bw.newLine();
			}
			
			bw.close();
			Log.d(LOG_TAG, "Файл записан");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
		private void createPdf(String sometext){
			// create a new document
			PdfDocument document = new PdfDocument();
			// crate a page description
			PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
			// start a page
			PdfDocument.Page page = document.startPage(pageInfo);
			Canvas canvas = page.getCanvas();
			Paint paint = new Paint();
			paint.setColor(Color.RED);
			canvas.drawCircle(50, 50, 30, paint);
			paint.setColor(Color.BLACK);
			canvas.drawText(sometext, 80, 50, paint);
			//canvas.drawt
			// finish the page
			document.finishPage(page);
// draw text on the graphics object of the page
			// Create Page 2
			pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 2).create();
			page = document.startPage(pageInfo);
			canvas = page.getCanvas();
			paint = new Paint();
			paint.setColor(Color.BLUE);
			canvas.drawCircle(100, 100, 100, paint);
			document.finishPage(page);
			// write the document content
			String directory_path = Environment.getExternalStorageDirectory().getPath() + "/mypdf/";
			File file = new File(directory_path);
			if (!file.exists()) {
				file.mkdirs();
			}
			String targetPdf = directory_path+"test-2.pdf";
			File filePath = new File(targetPdf);
			try {
				document.writeTo(new FileOutputStream(filePath));
				Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
			} catch (IOException e) {
				Log.e("main", "error "+e.toString());
				Toast.makeText(this, "Something wrong: " + e.toString(),  Toast.LENGTH_LONG).show();
			}
			// close the document
			document.close();
		}
		
}

