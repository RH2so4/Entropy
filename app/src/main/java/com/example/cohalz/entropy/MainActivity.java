package com.example.cohalz.entropy;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {


    int flag = 0;
    String ps[] = new String[2];
    int pastx, pasty;
    String gray = "#eeeeee";
    //String p2 = "#ff57ff6a";
    //String p = p1;
    int ban = 1;
    String white = "#ffffff";
    TextView view[][] = new TextView[5][5];
    int board[][] = new int[5][5]; //盤面を記憶する
    //1が1P,0が白,-1が2P,2が移動可能マス
    private BluetoothAdapter mBtAdapter;
    private TextView mResultView;
    private ArrayAdapter<String> mServers;
    private ArrayAdapter<String> mCandidateServers;
    private Bt mBt  = new Bt(this, mCandidateServers, mServers);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ps[0] = "#ff48fffd";
        ps[1] = "#ff57ff6a";
        view[0][0] = (TextView) findViewById(R.id.textView0);
        view[0][1] = (TextView) findViewById(R.id.textView1);
        view[0][2] = (TextView) findViewById(R.id.textView2);
        view[0][3] = (TextView) findViewById(R.id.textView3);
        view[0][4] = (TextView) findViewById(R.id.textView4);
        view[1][0] = (TextView) findViewById(R.id.textView5);
        view[1][1] = (TextView) findViewById(R.id.textView6);
        view[1][2] = (TextView) findViewById(R.id.textView7);
        view[1][3] = (TextView) findViewById(R.id.textView8);
        view[1][4] = (TextView) findViewById(R.id.textView9);
        view[2][0] = (TextView) findViewById(R.id.textView10);
        view[2][1] = (TextView) findViewById(R.id.textView11);
        view[2][2] = (TextView) findViewById(R.id.textView12);
        view[2][3] = (TextView) findViewById(R.id.textView13);
        view[2][4] = (TextView) findViewById(R.id.textView14);
        view[3][0] = (TextView) findViewById(R.id.textView15);
        view[3][1] = (TextView) findViewById(R.id.textView16);
        view[3][2] = (TextView) findViewById(R.id.textView17);
        view[3][3] = (TextView) findViewById(R.id.textView18);
        view[3][4] = (TextView) findViewById(R.id.textView19);
        view[4][0] = (TextView) findViewById(R.id.textView20);
        view[4][1] = (TextView) findViewById(R.id.textView21);
        view[4][2] = (TextView) findViewById(R.id.textView22);
        view[4][3] = (TextView) findViewById(R.id.textView23);
        view[4][4] = (TextView) findViewById(R.id.textView24);
        toBoard();

        mResultView = (TextView)findViewById(R.id.bt_text);
        // インテントフィルタの作成
        //IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        // ブロードキャストレシーバの登録
        //registerReceiver(mBt.mReceiver, filter);
        mServers = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mCandidateServers = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);



    }



    @Override
    protected void onResume() {
        super.onResume();
        mBt.turnOn();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mBt.onActivityResult(requestCode, resultCode, data);
    }
    public void onClick(View v) {

        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                if (v == view[y][x]) {
                    Log.i("v", "x:" + x + ", y:" + y);
                    if (flag == 0 && board[y][x] == ban) {
                        if (isTouched(x, y, ban)) {

                            if (movable(x,y) > 0) {
                                flag = 1;
                                pastx = x;
                                pasty = y;

                            }
                        }
                    } else if (flag == 1) {

                        if (board[y][x] == 2) {
                            board[pasty][pastx] = 0;
                            board[y][x] = ban;
                            ban = ban * -1;
                        }

                        for (int i = 0; i < 5; i++) {
                            for (int j = 0; j < 5; j++) {
                                if (board[i][j] == 2)
                                    board[i][j] = 0;
                            }
                        }
                        flag = 0;
                    }
                }
            }
        }
        display();
    }

    //引数の地点からどこに移動できるかのフラグを作成する
    //戻り値は移動できる場所の数
    public int movable(int x, int y) {
        int count = 0;
        int i = 1;
        while (y - i >= 0 & x - i >= 0 && board[y - i][x - i] == 0) {
            board[y - i][x - i] = 2;
            i++;
            count++;
        }
        i = 1;
        while (y + i < 5 && x + i < 5 && board[y + i][x + i] == 0) {
            board[y + i][x + i] = 2;
            i++;
            count++;
        }
        i = 1;
        while (y - i >= 0 && x + i < 5 && board[y - i][x + i] == 0) {
            board[y - i][x + i] = 2;
            i++;
            count++;
        }
        i = 1;
        while (y + i < 5 && x - i >= 0 && board[y + i][x - i] == 0) {
            board[y + i][x - i] = 2;
            i++;
            count++;
        }
        i = 1;
        while (y + i < 5 && board[y + i][x] == 0) {
            board[y + i][x] = 2;
            i++;
            count++;
        }
        i = 1;
        while (y - i >= 0 && board[y - i][x] == 0) {
            board[y - i][x] = 2;
            i++;
            count++;
        }
        i = 1;
        while (x - i >= 0 && board[y][x - i] == 0) {
            board[y][x - i] = 2;
            i++;
            count++;
        }
        i = 1;
        while (x + i < 5 && board[y][x + i] == 0) {
            board[y][x + i] = 2;
            i++;
            count++;
        }
        return count;
    }

    public boolean isTouched(int x, int y, int ban) {
        if (y > 0) {
            if (board[y - 1][x] == ban) return true;
            if (x > 0) {
                if (board[y - 1][x - 1] == ban) return true;
            }
            if (x < 4) {
                if (board[y - 1][x + 1] == ban) return true;
            }
        }
        if (y < 4) {
            if (board[y + 1][x] == ban) return true;
            if (x > 0) {
                if (board[y + 1][x - 1] == ban) return true;
            }
            if (x < 4) {
                if (board[y + 1][x + 1] == ban) return true;
            }
        }
        if (x > 0) {
            if (board[y][x - 1] == ban) return true;
        }
        if (x < 4) {
            if (board[y][x + 1] == ban) return true;
        }
        return false;
    }

    public void display() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (board[i][j] == 1) view[i][j].setBackgroundColor(Color.parseColor(ps[0]));
                else if (board[i][j] == -1) view[i][j].setBackgroundColor(Color.parseColor(ps[1]));
                else if (board[i][j] == 0) view[i][j].setBackgroundColor(Color.parseColor(white));
                else if (board[i][j] == 2) view[i][j].setBackgroundColor(Color.parseColor(gray));
            }
        }
    }

    public void toBoard() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int  color = ((ColorDrawable) view[i][j].getBackground()).getColor();
                if (color == Color.parseColor(ps[0])) board[i][j] = 1 ;
                if (color == Color.parseColor(ps[1])) board[i][j] = -1;
                if (color == Color.parseColor(white)) board[i][j] = 0 ;
                if (color == Color.parseColor(gray))  board[i][j] = 2;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_discoverable) {
            mBt.startDiscoverable();
            return true;
        }else if (id == R.id.menu_start_server) {
            mBt.startServer();
        }else if (id == R.id.menu_search_server) {
            //mCandidateServers.clear();
            ListView lv = new ListView(this);
            lv.setAdapter(mCandidateServers);
            lv.setScrollingCacheEnabled(false);
            final AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.title_dialog)
                    .setPositiveButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mBt.cancelDiscovery();
                        }
                    })
                    .setView(lv)
                    .create();
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> items, View view, int position, long id) {
                    dialog.dismiss();
                    String address = mCandidateServers.getItem(position);
                    if (mServers.getPosition(address) == -1) {
                        mServers.add(address);
                    }
                    mBt.cancelDiscovery();
                }
            });
            dialog.show();
            mBt.searchServer();
        }else if(id == R.id.menu_connect){
            ListView lv = new ListView(this);
            final AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.title_dialog)
                    .setPositiveButton(android.R.string.cancel, null)
                    .setView(lv)
                    .create();
            lv.setAdapter(mServers);
            lv.setScrollingCacheEnabled(false);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> items, View view, int position, long id) {
                    dialog.dismiss();
                    String address = mServers.getItem(position);
                    mBt.connect(address);
                }
            });
            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

}
