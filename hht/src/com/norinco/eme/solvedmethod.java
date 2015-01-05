package com.norinco.eme;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cngc.hht.MyApp;
import com.cngc.hht.R;
import com.cngc.hht.SingleLabel;

@SuppressLint("NewApi")
public class solvedmethod extends Activity {
    MyApp myApp;

    private final static String TAG = solvedmethod.class.getSimpleName();

    String methods;

    // String solvedmethods;

    int scroll_num = 10;
    HashMap<Integer, Boolean> isSelected;
    ArrayList<HashMap> data;
    HashMap map;
    HashMap deviceMap;

    FaceProData faceProData;

    ListView mListView;
    Button btn_all;
    Button btn_none;

    private static boolean DataBaseFlag = false;

    private static boolean solvedFlag = false;

    private static int miCount = 0;

    private int off = R.drawable.circle_delete;
    private int on = R.drawable.circle_ok;
    private int init = R.drawable.circle;

    private TextView t1;
    private EditText e1;

    /* 报修人与维修人 */
    private EditText edit_report;
    private EditText edit_fix;

    private Button mButtonOk = null;
    private Button mButtonFault = null;

    private Button mSave = null;
    private Button mBack = null;

    ArrayList<ListStatus> arrList = new ArrayList<ListStatus>();

    ListView m_ListView = null;
    List<HashMap<String, Object>> mList = null;
    SimpleAdapter adapter2;

    private int selectItem;
    private boolean flag = false;
    private int countMethod = 0;
    // MethodData methodData;
    MethodDataV2 m2;

    boolean c1_stat = false;
    boolean c2_stat = false;
    boolean c3_stat = false;
    boolean c4_stat = false;
    boolean c5_stat = false;

    private int l = 3;
    private String user;

    String problem;
    String solvedmethods;
    String reporter = null;
    String fixer = null;

    Button saveWithXml = null;

    // ListView listV = null;

    // MethodData methodData;

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        // mSQLiteDatabase.close();
        faceProData.close();
        // methodData.close();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        faceProData.close();
        super.onPause();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        faceProData.close();
        super.onStop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println(TAG + "start!!!");
        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();
        int position = bundle.getInt("position");
        problem = bundle.getString("problem");
        //user = bundle.getString("user");
        //l = bundle.getInt("level");
        deviceMap = (HashMap) intent.getSerializableExtra("map");
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.solvedmethod);

        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
        TextView titleTextView = (TextView) findViewById(R.id.title_text);
        titleTextView.setText(R.string.update_fault);
		
        ((TextView) findViewById(R.id.solvedmethod)).setText(problem);
       
        ((TextView) findViewById(R.id.basetv_devname)).setText(deviceMap
                .get("devname") + "");
        ((TextView) findViewById(R.id.basetv_devnum)).setText(deviceMap
                .get("number") + "");
        ((TextView) findViewById(R.id.basetv_group)).setText(deviceMap
                .get("devgroup") + "");
        ((TextView) findViewById(R.id.basetv_factory)).setText(deviceMap
                .get("factory") + "");
        ((TextView) findViewById(R.id.basetv_prodate)).setText(deviceMap
                .get("productdate") + "");
        ((TextView) findViewById(R.id.basetv_recdate)).setText(deviceMap
                .get("recvdate") + "");

        // mListView = (ListView) findViewById(R.id.main_listview);

        // 设置按钮点击事件
        // init_btn();
        // 设置listview
        // init_listview();
        edit_report = (EditText) findViewById(R.id.edit_reporter);
        edit_fix = (EditText) findViewById(R.id.edit_fixer);

        m_ListView = (ListView) findViewById(R.id.method_list);

        update(position);

        // methodData = new MethodData(this);
        m2 = new MethodDataV2(this);

        mSave = (Button) findViewById(R.id.isave);
        mBack = (Button) findViewById(R.id.iback);

        mSave.setOnClickListener(bListenler);
        mBack.setOnClickListener(bListenler);

        Resources res = getResources();
        // Drawable drawable = res.getDrawable(R.drawable.android_background);
        Drawable drawable = res.getDrawable(R.color.white);
        this.getWindow().setBackgroundDrawable(drawable);

    }

    /* 点击保存和退出按钮 */
    View.OnClickListener bListenler = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            reporter = edit_report.getText().toString();
            fixer = edit_fix.getText().toString();
            if (v == mSave) {
                /* 保存解决方案 */
                System.out.println(flag);

                if (flag == false) {
                    Dialog dlg = new AlertDialog.Builder(solvedmethod.this)
                            .setTitle("友情提示")
                            .setMessage("您还没有选择任何方案")
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            // TODO Auto-generated method stub

                                        }
                                    }).create();
                    dlg.show();
                }

                boolean status = false;
                for (int i = 0; i < countMethod; i++) {

                    if (arrList.get(i).isStatus()) {
                        status = true;
                        System.out.println("解决方案是：" + status);
                    }
                }
                // System.out.println(arrList.get(i).isStatus());
                if (status) {
					/* 生成维修历史 */
                    System.out.println("!!!!!!!!!!" + problem);
                    // insertData(arrList.get(i).getId(), methods);
                    String Datamethods = problem;
                    // methodData.insert(Datamethods, Datamethods);

                    System.out.println("生成故障解决方案！！！！");

                } else {
					/* 故障未解决 */
                    System.out.println("故障未解决" + countMethod + problem);
                    // methodData.insert(problem, "未解决");
					/* 生成故障未解决办法 */
                    if ("".equals(reporter.trim())) {
                        Toast.makeText(solvedmethod.this,
                                "请输入报修人姓名", Toast.LENGTH_SHORT).show();
                    }if ("".equals(fixer.trim())){
                        Toast.makeText(solvedmethod.this,
                                "请输入维修人姓名", Toast.LENGTH_SHORT).show();
                    }else {

                        m2.insert(Integer.valueOf(deviceMap.get("number") + ""),
                                deviceMap.get("factory") + "",
                                deviceMap.get("devname") + "", reporter, fixer,
                                deviceMap.get("productdate") + "",
                                deviceMap.get("recvdate") + "",
                                getCurrentDate() + "", problem, "未解决");
                    }
                }
                Toast.makeText(solvedmethod.this, "方案已保存", Toast.LENGTH_SHORT)
                        .show();
            } else if (v == mBack) {
				/* 直接退出 */
                if (flag == false) {
                    Dialog dlg = new AlertDialog.Builder(solvedmethod.this)
                            .setTitle("友情提示")
                            .setMessage("您还没有选择任何方案,确认退出吗？")
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            // TODO Auto-generated method stub
                                            Intent intent = new Intent();
                                            //intent.putExtra("level", l);
                                            //intent.putExtra("user", user);
                                            //intent.putExtra("map", deviceMap);
                                            intent.setClass(solvedmethod.this,
                                                    SingleLabel.class);
                                            startActivity(intent);
                                            //finish();

                                        }
                                    })
                            .setNeutralButton("退出",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            // TODO Auto-generated method stub

                                        }
                                    }).create();
                    dlg.show();
                } else {
                    Toast.makeText(solvedmethod.this, "直接退出",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    //intent.putExtra("level", l);
                    //intent.putExtra("user", user);
                    intent.setClass(solvedmethod.this,
                            SingleLabel.class);
                    startActivity(intent);
                    //finish();
                }
            }
        }

    };

    private void update(int num) {

        faceProData = new FaceProData(this);
        // Cursor m_consor = fetchData(num);
        System.out.println(num);

        mList = new ArrayList<HashMap<String, Object>>();
        Cursor cur_all = faceProData.fetchData(num);
        // Cursor cur_all = faceProData.fetchByProName(problem);
        int rows_num = cur_all.getCount();
        for (int i = 0; i < rows_num; i++) {
            ListStatus lStatus = new ListStatus();
            lStatus.setId(i);
            lStatus.setStatus(false);
            arrList.add(lStatus);
        }
        countMethod = rows_num;

        for (int i = 0; i < rows_num; i++) {
            System.out.println(arrList.get(i).getId());
        }

        if (rows_num != 0) {
            cur_all.moveToFirst();
            for (int i = 0; i < rows_num; i++) {
                int _id = cur_all.getInt(0);
                // int num = cur_all.getInt(1);
                String value = cur_all.getString(3);
                System.out.println(value);
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("image", init);
                map.put("text", value);
                mList.add(map);
                cur_all.moveToNext();
            }
        }

        adapter2 = new SimpleAdapter(solvedmethod.this, mList,
                R.layout.item_listview2, new String[]{"image", "text"},
                new int[]{R.id.img, R.id.title});
        m_ListView.setAdapter(adapter2);
        cur_all.close();

        m_ListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                flag = true;
                selectItem = position;
                solvedmethods = (String) mList.get(position).get("text");
                switch (position) {
                    case 0:
                        showDialog();
                        break;
                    case 1:
                        showDialog();
                        break;
                    case 2:
                        showDialog();
                        break;
                    case 3:
                        showDialog();
                        break;
                    case 4:
                        showDialog();
                        break;
                    default:
                        showDialog();
                        break;
                }

            }

        });
    }

    private void showDialog() {

        reporter = edit_report.getText().toString();
        fixer = edit_fix.getText().toString();

        LayoutInflater factory = LayoutInflater.from(solvedmethod.this);
        final View DialogView = factory.inflate(R.layout.dialogview, null);
        AlertDialog dialog = new AlertDialog.Builder(solvedmethod.this)
                .setTitle("解决方案")
                .setView(DialogView)
                .setPositiveButton("可用", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        arrList.get(selectItem).setStatus(true);
                        solvedFlag = true;
                        if (solvedFlag) {
                            if (!("".equals(reporter.trim())) && !("".equals(fixer.trim()))) {
                                String Datamethods = problem;
                                System.out.println("aaaaaaaaaaaaaaaaaaaaaaa!!!!!");
                                // methodData.insert(Datamethods, solvedmethods);
							/* 生成故障解决办法 */
                                m2.insert(
                                        Integer.valueOf(deviceMap.get("number")
                                                + ""), deviceMap.get("factory")
                                        + "",
                                        deviceMap.get("devname") + "", reporter,
                                        fixer, deviceMap.get("productdate") + "",
                                        deviceMap.get("recvdate") + "",
                                        getCurrentDate(), Datamethods,
                                        solvedmethods);
                                // m2.insert(
                                // Integer.valueOf(deviceMap.get("number")
                                // + ""), Datamethods, solvedmethods);

                                Intent intent = new Intent();
                                //intent.putExtra("user", user);
                                //intent.putExtra("level", l);
                                //intent.putExtra("map", deviceMap);
                                //intent.putExtra("display", problem);
                                System.out.println("0000000000" + problem
                                        + solvedmethods);
                                //intent.putExtra("solved", solvedmethods);
                                intent.setClass(solvedmethod.this,
                                        SingleLabel.class);
                                startActivity(intent);
                                ChangeImg(selectItem, true);
                            } else {
                                if ("".equals(reporter.trim())) {
                                    Toast.makeText(solvedmethod.this,
                                            "请输入报修人姓名", Toast.LENGTH_SHORT).show();
                                }
                                if ("".equals(fixer.trim())) {
                                    Toast.makeText(solvedmethod.this,
                                            "请输入维修人姓名", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        

                    }
                })
                .setNeutralButton("不可用", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        arrList.get(selectItem).setStatus(false);
                        solvedFlag = false;
                        ChangeImg(selectItem, false);

                    }

                }).create();
        dialog.show();
    }

    private void ChangeImg(int selectedItem, boolean b) {
        SimpleAdapter la = adapter2;
        System.out.println(selectedItem);
        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) la
                .getItem(selectedItem);
        if (b) {
            map.put("image", on);
        } else {
            map.put("image", off);
        }
        la.notifyDataSetChanged();
    }

    String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss ");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String sysDate = formatter.format(curDate);
        return sysDate;
    }
}