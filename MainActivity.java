package com.example.version5;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    ArrayList<ListItem> lists=new ArrayList<ListItem>();
    public static final String EXTRA_MESSAGE = "com.example.test.MESSAGE";
    ReadContact contactInformation = new ReadContact(MainActivity.this);
    final WriteTxtofPosition writeData = new WriteTxtofPosition();
    final ReadTxtofPosition readData = new ReadTxtofPosition();
    ListView listView;
    ListViewAdapter listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        //get listView
        listView=(ListView)findViewById(R.id.listView);
        contactInformation.initview(MainActivity.this,this);
        initListItem();//initialize data

        //first arg: context; second arg: layout's id; third arg: adapter content
        listViewAdapter=new ListViewAdapter(MainActivity.this,R.layout.
               item_activity,lists,this);
        //add xml to the listview
        listView.setAdapter(listViewAdapter);

        new ReadFilesTask().execute();

        /*
        //new thread
        //ReadPosThread myThread = new ReadPosThread();
        //myThread.start();

        //create a new thread in onClicklistener.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,final int position, long id) {
                new Thread(new Runnable() {
                    public void run() {
                        // a potentially time consuming task
                        ListItem listItem = lists.get(position);
                        writeData.writeData(""+position,MainActivity.this);

                        //new thread communicate with UI thread through view.post method
                        listView.post(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(MainActivity.this, DisplayActivity.class);
                                String message = contactInformation.idadd()[position] + "+" + contactInformation.titleadd()[position] + "+" + contactInformation.valueadd()[position];
                                //add value to Intent
                                intent.putExtra(EXTRA_MESSAGE, message);
                                startActivity(intent);
                            }
                        });
                        //update adapter then jump to the selected line
                        //listViewAdapter.notifyDataSetChanged();
                        //listView.setSelection(1);
                    }
                }).start();
            }
        });
         */
    }

    //three types used by an asynchronous task:Params,Progress,Result
    //Params, type of the parameters sent to the task upon execution
    //Progress, the type of the progress units published during the background computation
    //Result, the type of the result of the background computation
    //!!!!!! Normally only can use once, otherwise, errors occur.
    private class ReadFilesTask extends AsyncTask<Void,Integer,Integer>{
        // invoked on the UI thread before the task is executed
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        // invoked on the background thread immediately after onPreExecute() finishes executing.
        @Override
        protected Integer doInBackground(Void...voids) {
            ReadTxtofPosition readData = new ReadTxtofPosition();
            List dataList = readData.Txt(MainActivity.this);
            return Integer.parseInt(dataList.get(dataList.size()-1).toString());
        }

        // invoked on the UI thread after a call to publishProgress(Progress...). The timing of the execution is undefined.
        // This method is used to display any form of progress in the user interface while the background computation is still executing.
        @Override
        protected void onProgressUpdate(Integer... progress){
        }

        // invoke on the UI thread after the background computation finishes,
        // The result of the background computation is passed to this step as a parameter.
        // can operate UI thread here which belongs to the UI Main thread
        @Override
        protected void onPostExecute(Integer result){
            listView.setSelection(result);
        }
    }

    public void initListItem(){
        for (int i = 0; i<=contactInformation.contactsId.size()-1; i++) {
            lists.add(new ListItem(R.drawable.image5,""+contactInformation.titleadd()[i],""+contactInformation.valueadd()[i]));
        }
    }
}
