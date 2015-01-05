package com.norinco.eme;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cngc.hht.MyApp;
import com.cngc.hht.R;
//import com.norinco.device.DeviceViewActivity;
//import com.norinco.device.MDeviceAddActivity;
//import com.norinco.device.MessageListActivity;

@SuppressLint("NewApi")
public class face_Activity001 extends Activity {
    MyApp myApp;

    String methods;

    int scroll_num = 10;
    ViewHolder holder;
    HashMap<Integer, Boolean> isSelected;
    MyAdapter adapter;
    ArrayList<HashMap> data;
    HashMap map;
    HashMap deviceMap;

    private static String TAG = face_Activity001.class.getSimpleName();

    FaceProData faceProData;

    ListView mListView;
    Button btn_all;
    Button btn_none;

    String otherProblem = null;
    String reporter = null;
    String fixer = null;

    private static boolean DataBaseFlag = false;

    private static int miCount = 0;

    private int off = R.drawable.circle_delete;
    private int on = R.drawable.circle_ok;
    private int init = R.drawable.circle;

    private static int checkbox_c1 = 1;
    private static int checkbox_c2 = 2;
    private static int checkbox_c3 = 3;
    private static int checkbox_c4 = 4;
    private static int checkbox_c5 = 5;

    String[] table_data = {"操作系统启动不了，蓝屏", "系统自动重启", "开机有警报声，一长两短",
            "开机不进系统，显示兵器log不动", "显示字符出现乱码"};
    String[] table_method = {"按F2进入bios设置启动选项，如不成功则需要一键恢复系统", "重装操作系统",
            "BIOS出错，重写BIOS程序", "BIOS或南桥BIOS重写", "编码格式重新设置"};

    String[] table_method1 = {"aaaa", "bbbb", "cccc", "dddd", "eeee"};
    String[] table_method2 = {"重启机器", "重启系统", "重启BIOS", "换一个系统", "修改文本中的字符"};

    private TextView t1;
    private EditText e1;
    private EditText edit_reporter;
    private EditText edit_fixer;

    private Button mButtonOk = null;
    private Button mButtonFault = null;

    private Button mSave = null;
    private Button mBack = null;

    ArrayList<ListStatus> arrList = new ArrayList<ListStatus>();

    // ListView m_ListView = null;
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
    private String user = "admin";

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        // mSQLiteDatabase.close();
        m2.close();
        // methodData.close();
        faceProData.close();
        super.onDestroy();
    }

    private void initDatabase() {
        faceProData = new FaceProData(this);
        if (faceProData.count() == 0) {
            for (int i = 0; i < table_data.length; i++) {
                faceProData.insert(i, table_data[i], table_method[i]);
                faceProData.insert(i, table_data[i], table_method1[i]);
                faceProData.insert(i, table_data[i], table_method2[i]);
            }
        }
        if (DataBaseFlag) {
            for (int i = 0; i < table_data.length; i++) {
                faceProData.insert(i, table_data[i], table_method1[i]);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        //l = bundle.getInt("level");
        //user = bundle.getString("user");
        deviceMap = (HashMap) intent.getSerializableExtra("device");
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.newfragment1);

        // methodData = new MethodData(this);
        m2 = new MethodDataV2(this);

        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
        TextView titleTextView = (TextView) findViewById(R.id.title_text);
        titleTextView.setText(R.string.update_fault);

        // myApp = (MyApp) getApplication();

        // deviceMap = myApp.gethMap();
        // EquipName = map.get("devname");
        // Log.v(TAG, EquipName);
        // PrimaryKey = map.get("number");

		/* test */
        // ((TextView) findViewById(R.id.basetv_devname)).setText("aaaa");
        // ((TextView) findViewById(R.id.basetv_devnum)).setText("bbbb");
        // ((TextView) findViewById(R.id.basetv_group)).setText("cccc");
        // ((TextView) findViewById(R.id.basetv_factory)).setText("dddd");
        // ((TextView) findViewById(R.id.basetv_prodate)).setText("eeee");
        // ((TextView) findViewById(R.id.basetv_recdate)).setText("ffff");
        // ((TextView) findViewById(R.id.basetv_modifydate)).setText("gggg");
        /* test */

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

        mListView = (ListView) findViewById(R.id.main_listview);

        e1 = (EditText) findViewById(R.id.edit_message);
        edit_reporter = (EditText) findViewById(R.id.edit_reporter);
        edit_fixer = (EditText) findViewById(R.id.edit_fixer);
        mButtonOk = (Button) findViewById(R.id.checkbutton);
        mButtonOk.setOnClickListener(bListenler);

        // otherProblem = e1.getText().toString();

        initDatabase();

        // 初始化数据
        init_data();
        // 设置按钮点击事件
        // init_btn();
        // 设置listview
        init_listview();

        // m_ListView = (ListView) findViewById(R.id.method_list);

        // mSave = (Button) findViewById(R.id.isave);
        // mBack = (Button) findViewById(R.id.iback);

        // mSave.setOnClickListener(bListenler);
        // mBack.setOnClickListener(bListenler);

        Resources res = getResources();
        // Drawable drawable = res.getDrawable(R.drawable.android_background);
        Drawable drawable = res.getDrawable(R.color.white);
        this.getWindow().setBackgroundDrawable(drawable);

    }

    // 初始化数据
    public void init_data() {
        data = new ArrayList<HashMap>();
        Cursor cursor = faceProData.fetchProData();
        int rows_num = cursor.getCount();

        // for (int i = 0; i < rows_num; i++) {
        // System.out.println(arrList.get(i).getId());
        // }

        if (rows_num != 0) {
            cursor.moveToFirst();
            for (int i = 0; i < rows_num; i++) {
                int _id = cursor.getInt(0);
                int num = cursor.getInt(1);
                String pro = cursor.getString(2);
                String value = cursor.getString(3);
                System.out.println("sssssssssss" + num);
                System.out.println(value);
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("num", num);
                map.put("title", pro);
                map.put("content", value);
                data.add(map);
                cursor.moveToNext();
            }
        }
        cursor.close();
    }

    // 设置listview
    public void init_listview() {
        adapter = new MyAdapter(face_Activity001.this, data);
        mListView.setAdapter(adapter);
        // 为listview的子项添加点击事件
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                System.out.println("您所点击的行号是：" + position);
                methods = (String) data.get(position).get("title");
                position = (Integer) data.get(position).get("num");
                System.out.println("所点击的行号是" + position);

                Intent intent = new Intent();
                intent.setClass(face_Activity001.this, solvedmethod.class);
                intent.putExtra("position", position);
                intent.putExtra("map", deviceMap);
                intent.putExtra("user", user);
                intent.putExtra("lever", l);
                intent.putExtra("problem", methods);
                startActivity(intent);
                finish();

                System.out.println("-------------------------" + methods);
                miCount = position;
                // update(position);
            }
        });
    }

	/* 点击保存和退出按钮 */

    View.OnClickListener bListenler = new OnClickListener() {

        @Override
        public void onClick(View v) { // TODO Auto-generated method stub
            otherProblem = e1.getText().toString();
            reporter = edit_reporter.getText().toString();
            fixer = edit_fixer.getText().toString();

            Dialog dlg = new AlertDialog.Builder(face_Activity001.this)
                    .setTitle("添加故障现象")
                    .setMessage(otherProblem)
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub
                                    if (checkEditText()) {
                                        System.out.println(TAG + ":"
                                                + otherProblem);

                                        // methodData.insert(otherProblem,
                                        // "未解决");
                                        m2.insert(
                                                Integer.valueOf(deviceMap
                                                        .get("number") + ""),
                                                deviceMap.get("factory") + "",
                                                deviceMap.get("devname") + "",
                                                reporter,
                                                fixer,
                                                deviceMap.get("productdate") + "",
                                                deviceMap.get("recvdate") + "",
                                                getCurrentDate(),
                                                otherProblem, "未解决");
                                    } else {
                                        if ("".equals(reporter.trim())) {
                                            Toast.makeText(face_Activity001.this, "请输入报修人姓名",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                        if ("".equals(fixer.trim())){
                                            Toast.makeText(face_Activity001.this, "请输入维修人姓名",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                        if ("".equals(otherProblem.trim())) {
                                            Toast.makeText(face_Activity001.this, "请输入未解决的故障!",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                        System.out.println(TAG + ":未输入任何解决方案！");
                                    }
                                    // methodData.insert(otherProblem, "未解决");
                                }
                            })
                    .setNeutralButton("修改",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub
                                    e1.setText("");

                                }
                            }).create();
            dlg.show();
        }
    };

    boolean checkEditText() {
        boolean flag = false;
        if (("".equals(otherProblem.trim())) || ("".equals(reporter.trim())) || ("".equals(fixer.trim()))) {
            flag = false;
        } else {
            flag = true;
        }
        return flag;
    }

    String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss ");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String sysDate = formatter.format(curDate);
        return sysDate;
    }

	
    class MyAdapter extends BaseAdapter {
        int count = scroll_num;
        Context mContext;
        ArrayList<HashMap> mData;
        LayoutInflater mInflater;

        public MyAdapter(Context context, ArrayList<HashMap> data) {
            this.mContext = context;
            this.mData = data;
            mInflater = (LayoutInflater) mContext
                    .getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            if (count > mData.size()) {
                count = mData.size();
            }
            isSelected = new HashMap<Integer, Boolean>();
            for (int i = 0; i < data.size(); i++) {
                isSelected.put(i, false);
            }
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_listview, null);
                holder = new ViewHolder();
                holder.content = (TextView) convertView
                        .findViewById(R.id.item_listview_content);
                // holder.checkBox = (CheckBox) convertView
                // .findViewById(R.id.item_listview_checkbox);
                holder.image = (ImageView) convertView
                        .findViewById(R.id.item_listview_imageview);
                // holder.image.setImageAlpha(off);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.content.setText(data.get(position).get("title").toString());
            // holder.checkBox.setChecked(isSelected.get(position));
            // updateCheckBox(position);
            // holder.checkBox.setOnClickListener(new OnClickListener() {
            // @Override
            // public void onClick(View v) {
            // if (isSelected.get(position)) {
            // isSelected.put(position, false);
            // } else {
            // isSelected.put(position, true);
            // }
            // notifyDataSetChanged();
            // }
            // });

            return convertView;
        }
    }

    public void updateCheckBox(final int position) {
        System.out.println("0000000000000000000000000000" + position);
        final MyAdapter la = adapter;
        isSelected.put(position, true);
        // if (isSelected.get(position)) {
        // System.out.println("1111111111111111111111111" + position);
        // isSelected.put(position, true);
        // } else {
        // isSelected.put(position, false);
        // }
        la.notifyDataSetChanged();
    }

    class ViewHolder {
        TextView content;
        // CheckBox checkBox;
        ImageView image;
    }

}