
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

public class MainActivity extends  Activity implements OnClickListener
{
	private static final int PERMISSION_REQUEST_ID = 4;
	
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
	
	private Bitmap bitmap;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		PermissionUtil.checkAndRequest(this, PERMISSION_REQUEST_ID);
		StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
		StrictMode.setVmPolicy(builder.build());
		builder.detectFileUriExposure();
		
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
	
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

		if (requestCode == PERMISSION_REQUEST_ID &&
			grantResults[0] == PackageManager.PERMISSION_GRANTED &&
			grantResults[1] == PackageManager.PERMISSION_GRANTED){

			Toast.makeText(this, "PermissionResult ",Toast.LENGTH_SHORT).show();	

        } else {
            Toast.makeText(this, R.string.no_external_permissions, Toast.LENGTH_LONG).show();
        }
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
				//createPdf("test");
				bitmap = loadBitmapFromView(tblLayout, tblLayout.getWidth(), tblLayout.getHeight());
                createPdf();
				break;
				
			case R.id.mainDellRow:
				delRow();
				
				break;
		}

	}
	
	void addRow(){		
		String str = editText1.getText().toString();
		addRow(str);
	}
	
	// str = "xx.xx xx.xx"
	int addRow(String str){
		
		String tmp;
		String sep = ".";
		int res = 0;
		String[] s1 = str.split(" ");
		if(s1.length !=2){
			return 0;
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
		
		/*if(x>y) res = hm1-hm;
		else    res = hm-hm1;*/
		res = Math.abs( hm1-hm );
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
		return res;
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
			String str = "";
			// читаем содержимое
			while ((str = br.readLine()) != null) {
				tMin +=  addRow(str);
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
	
	public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }
	
	private void createPdf(){
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

        openGeneratedPDF();

    }

    private void openGeneratedPDF(){
		String directory_path = Environment.getExternalStorageDirectory().getPath();
        File file = new File(directory_path+"/pdffromScroll.pdf");
        if (file.exists())
        {
            Intent intent=new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try
            {
                startActivity(intent);
            }
            catch(ActivityNotFoundException e)
            {
                Toast.makeText(this, "No Application available to view pdf", Toast.LENGTH_LONG).show();
            }
        }
    }
		
}

