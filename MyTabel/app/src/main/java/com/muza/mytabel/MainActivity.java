
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
import com.muza.mytabel.Utils.PermissionUtil;
import android.content.pm.PackageManager;
import android.net.Uri;
import com.muza.mytabel.Utils.DataPicker;
import android.view.ViewDebug.IntToString;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.view.ViewTreeObserver.OnTouchModeChangeListener;

public class MainActivity extends  Activity implements OnClickListener {
	private static final int PERMISSION_REQUEST_ID = 4;

	final Context context = this;

	final String LOG_TAG = "myLogs";

	final String FILENAME = "file";

	final String DIR_SD = "MyFiles";
	final String FILENAME_SD = "fileSD";

	private int ROW = 0;
	private TableLayout tblLayout = null;
	private TableRow 	tableRow  = null;

	private Button btnSet;
	private Button btnClear;
	private Button btnAddRow;
	private Button btnDellRow;
	private Button btnSetPicker;

	private NumberPicker np1;
	private NumberPicker np2;
	private NumberPicker np3;
	private NumberPicker np4;

	private EditText editText1;
	private TextView tvInfo;

	private Bitmap bitmap;

	private AlertDialog.Builder mDialogBuilder;
	private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		PermissionUtil.checkAndRequest(this, PERMISSION_REQUEST_ID);
		StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
		StrictMode.setVmPolicy(builder.build());
		builder.detectFileUriExposure();

		//getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		tblLayout = findViewById(R.id.tableLayout);



		btnSet = findViewById(R.id.mainButtonSet);
		btnSet.setOnClickListener(this);
		btnClear = findViewById(R.id.mainButtonClear);
		btnClear.setOnClickListener(this);
		btnAddRow = findViewById(R.id.mainAddRow);
		btnAddRow.setOnClickListener(this);
		btnDellRow = findViewById(R.id.mainDellRow);
		btnDellRow.setOnClickListener(this);
		btnSetPicker = findViewById(R.id.mainButtonSetPicker);
		btnSetPicker.setOnClickListener(this);

	    np1 = (NumberPicker) findViewById(R.id.mainNumberPicker1);  
        np1.setMinValue(1);
        np1.setMaxValue(24);
		// np1.setWrapSelectorWheel(true);

		np2 = (NumberPicker) findViewById(R.id.mainNumberPicker2);  
        np2.setMinValue(0);
        np2.setMaxValue(59);
		//  np2.setWrapSelectorWheel(true);

		np3 = (NumberPicker) findViewById(R.id.mainNumberPicker3);  
        np3.setMinValue(1);
        np3.setMaxValue(24);
		//   np3.setWrapSelectorWheel(true);

		np4 = (NumberPicker) findViewById(R.id.mainNumberPicker4);  
        np4.setMinValue(0);
        np4.setMaxValue(59);
		//   np4.setWrapSelectorWheel(true);

		tvInfo = findViewById(R.id.mainTextView1);
		//Set TextView text color
        tvInfo.setTextColor(Color.parseColor("#ffd32b3b"));

		editText1 = findViewById(R.id.mainEditText1);
		/*editText1.addTextChangedListener(new TextWatcher(){
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
		 });*/
		editText1.addOnLayoutChangeListener(new OnLayoutChangeListener(){
				public void onLayoutChange(View v, int left, int top, int right, int bottom,
										   int oldLeft, int oldTop, int oldRight, int oldBottom) {
					//tvInfo.setText("hhh");
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


		//Получаем вид с файла prompt.xml, который применим для диалогового окна:
		LayoutInflater li = LayoutInflater.from(context);
		View promptsView = li.inflate(R.layout.prompt, null);

		//Создаем AlertDialog
	    mDialogBuilder = new AlertDialog.Builder(context);

		//Настраиваем prompt.xml для нашего AlertDialog:
		mDialogBuilder.setView(promptsView);

		//Настраиваем отображение поля для ввода текста в открытом диалоге:
		final EditText userInput = (EditText) promptsView.findViewById(R.id.input_text);

		//Настраиваем сообщение в диалоговом окне:
		mDialogBuilder
			.setCancelable(false)
			.setPositiveButton("OK",
			new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					//Вводим текст и отображаем в строке ввода на основном экране:
					editText1.setText(userInput.getText());
					final int count = tableRow.getChildCount();
					for (int i = 0; i < count; i++) {
						final TextView child = (TextView) tableRow.getChildAt(i);
						String text = child.getText().toString(); // текст, что там дальше с ним делать
						tvInfo.setText(text);
						//child.setBackgroundColor(Color.RED);
					}

					//Создаем AlertDialog:
					//AlertDialog alertDialog = mDialogBuilder.create();
					tableRow.setBackgroundColor(R.color.tableRowBackground);
				}
			})
			.setNegativeButton("Отмена",
			new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});

		//Создаем AlertDialog:
		alertDialog = mDialogBuilder.create();

		//и отображаем его:
		//alertDialog.show();



		//editText1.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_TEXT);
    }

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Toast.makeText(this, "newConfig ", Toast.LENGTH_SHORT).show();	
		tvInfo.setText("newconfig");
	}


	@Override
	protected void onResume() {
		readTable();
		super.onResume();
	}

	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

		if (requestCode == PERMISSION_REQUEST_ID &&
			grantResults[0] == PackageManager.PERMISSION_GRANTED &&
			grantResults[1] == PackageManager.PERMISSION_GRANTED) {

			Toast.makeText(this, "PermissionResult ", Toast.LENGTH_SHORT).show();	

        } else {
            Toast.makeText(this, R.string.no_external_permissions, Toast.LENGTH_LONG).show();
        }
	}

	@Override
	public void onClick(View v) {
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
				//createPdf("test");
				bitmap = loadBitmapFromView(tblLayout, tblLayout.getWidth(), tblLayout.getHeight());
                createPdf();
				break;

			case R.id.mainDellRow:
				delRow();
				break;

			case R.id.mainButtonSetPicker:
				pressSetPicker();
				break;
		}

	}

	void pressSetPicker() {
		String str = Integer.toString(np1.getValue()) + "." 
			+ Integer.toString(np2.getValue()) + " "
			+ Integer.toString(np3.getValue()) + "."
			+ Integer.toString(np4.getValue());
		addRow(str);
	}
	void addRow() {		
		String str = editText1.getText().toString();
		addRow(str);
	}

	// str = "xx.xx xx.xx"
	int addRow(String str) {

		String tmp;
		String sep = ".";
		int res = 0;
		String[] s1 = str.split(" ");
		if (s1.length != 2) {
			return 0;
		}
		float x = Float.parseFloat(s1[0]);
		float y = Float.parseFloat(s1[1]);

		int h = (int)x;
		tmp   = Float.toString(x).split("\\.")[1];
		int m = Integer.parseInt(tmp);
		if (tmp.length() == 1)
			m = Integer.parseInt(tmp) * 10;
		//tvInfo.append(tmp + " " + m + " " + tmp.length());

		int h1 = (int)y;
		tmp   = Float.toString(y).split("\\.")[1];
		int m1 = Integer.parseInt(tmp);
		if (tmp.length() == 1)
			m1 = Integer.parseInt(tmp) * 10;

		int hm  = h * 60 + m;
		int hm1 = h1 * 60 + m1;

		/*if(x>y) res = hm1-hm;
		 else    res = hm-hm1;*/
		res = Math.abs(hm1 - hm);
		int hour = res / 60;
		int min  = res - hour * 60;


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
		tv4.setText(Integer.toString(hour) + sep + Integer.toString(min));

		tv1.setText(Integer.toString(tblLayout.getChildCount() + 1));

		tblLayout.addView(tr);
		tr.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					//row = null;
					//try {
					tvInfo.setText(v.getClass().toString());
                    TableRow row = (TableRow) v;
					tableRow = (TableRow) v;
					//} catch (Exception e) {
					//   System.out.println(e);
					//}
					//if (row!=null){
					final int count = row.getChildCount();
					for (int i = 0; i < count; i++) {
						final TextView child = (TextView) row.getChildAt(i);
						String text = child.getText().toString(); // текст, что там дальше с ним делать
						tvInfo.setText(text);
						//child.setBackgroundColor(Color.RED);
					}

					//Создаем AlertDialog:
					//AlertDialog alertDialog = mDialogBuilder.create();
					row.setBackgroundColor(Color.RED);
					alertDialog.show();

					//}
				}

			});

		tr.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent e) {

					return false;
				}
			});
		return res;
	}

	void delRow() {
		int i = tblLayout.getChildCount() - 1;
		if (i >= 0)
			tblLayout.removeViewAt(i);
	}

	void readTable() {
		try {

			int tMin =0;
			// открываем поток для чтения
			BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput(FILENAME_SD)));

			tblLayout.removeAllViews();
			String str = "";
			// читаем содержимое
			while ((str = br.readLine()) != null) {
				tMin +=  addRow(str);
			}
			int thour = tMin / 60;
			int tmin  = tMin - thour * 60;
			tvInfo.append("" + thour + ":" + tmin + " ");		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void writeTable() {

		try {
			int rowcount = tblLayout.getChildCount();
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(openFileOutput(FILENAME_SD, MODE_PRIVATE)));

			for (int row = 0; row < rowcount; row++) {
				TableRow tbr = (TableRow) tblLayout.getChildAt(row);

				TextView tv = (TextView) tbr.getChildAt(1);
				String res1 = tv.getText().toString();
				tv = (TextView) tbr.getChildAt(2);
				String res2 = tv.getText().toString();
				bw.write(res1 + " " + res2);	
				if (row != rowcount - 1) bw.newLine();
			}

			bw.close();
			Log.d(LOG_TAG, "Файл записан");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }

	private void createPdf() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        //  Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        float hight = tblLayout.getWidth();// Math.max(displaymetrics.heightPixels, displaymetrics.widthPixels); ;
        float width = tblLayout.getHeight(); // Math.min(displaymetrics.widthPixels, displaymetrics.heightPixels); ;

        int convertHighet = (int) width, convertWidth = (int) hight;
		tvInfo.append(" h= " + hight + " w= " + width + " ");
//        Resources mResources = getResources();
//        Bitmap bitmap = BitmapFactory.decodeResource(mResources, R.drawable.screenshot);

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        canvas.drawPaint(paint);

        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHighet, true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0 , null);
        document.finishPage(page);

        // write the document content
        String targetPdf = "/sdcard/pdffromScroll.pdf";
        File filePath;
        filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();
        Toast.makeText(this, "PDF of Scroll is created!!!", Toast.LENGTH_SHORT).show();

        //openGeneratedPDF();
		writeTable("test");
    }

    private void openGeneratedPDF() {
		String directory_path = Environment.getExternalStorageDirectory().getPath();
        File file = new File(directory_path + "/pdffromScroll.pdf");
        if (file.exists()) {
            Intent intent=new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No Application available to view pdf", Toast.LENGTH_LONG).show();
            }
        }
    }

	void writeTable(String fName) {

		try {
			int rowcount = tblLayout.getChildCount();
			String directory_path = Environment.getExternalStorageDirectory().getPath();
			String targetTable = "/sdcard/table.txt";
			BufferedWriter bw = new BufferedWriter(new FileWriter(targetTable));

			for (int row = 0; row < rowcount; row++) {
				TableRow tbr = (TableRow) tblLayout.getChildAt(row);

				TextView tv = (TextView) tbr.getChildAt(1);
				String res1 = tv.getText().toString();
				tv = (TextView) tbr.getChildAt(2);
				String res2 = tv.getText().toString();
				tv = (TextView) tbr.getChildAt(2);
				String res3 = tv.getText().toString();
				bw.write(res1 + " " + res2 + " " + res3);	
				bw.newLine();
			}
			bw.write(tvInfo.getText().toString());
			bw.close();
			Log.d(LOG_TAG, "Файл записан");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

