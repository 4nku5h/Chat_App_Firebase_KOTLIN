package com.example.nikhil.twitter

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.support.v7.app.ActionBar
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_.*
import kotlinx.android.synthetic.main.ticket_chat.view.*
import kotlinx.android.synthetic.main.ticket_chat_incomming.view.*
import kotlinx.android.synthetic.main.ticket_chat_send.view.*


class Chat_Activity : AppCompatActivity() {
    private var firebasedatabase=FirebaseDatabase.getInstance()
    private var myref=firebasedatabase.reference

    var friends_name:String=""
    var friends_email:String=""
    var my_email:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_)
        var b:Bundle= intent.extras
        friends_name=b.getString("name")
        friends_email=b.getString("friends_email")
        my_email=b.getString("my_email")

        var actionBar:ActionBar=supportActionBar as ActionBar
        actionBar.title=friends_name
        Message_Sent()
    }
    fun btn_send_message_event(view:View){

        try{
            var sent_message:String=et_send_message.text.toString()
            if(sent_message!="") {
                myref.child("twitter-2b40b").child(SplitString(friends_email)).child("Message").push()
                var pushref=myref.child("twitter-2b40b").child(SplitString(friends_email)).child("Message").push()
                var pushid=pushref.key as String
                pushref.child(SplitString(my_email)).setValue(sent_message)
                et_send_message.setText("")
                Message_Sent()

            }
        }catch (ex:Exception){}
    }

    fun Message_Sent(){

        var listOfChat= arrayListOf<ChatUser>()
        //sent

        myref.child("twitter-2b40b").child(SplitString(friends_email)).child("Message")
                .addChildEventListener(object :ChildEventListener{
                    override fun onCancelled(p0: DatabaseError?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onChildAdded(p0: DataSnapshot?, p1: String?) {

                        try{
                            var adapter_chat=GroupAdapter<ViewHolder>()

                            var td=p0!!.child(SplitString(my_email))
                            var message=td.value as String

                            if(message!=null && td.key==SplitString(my_email)) {
                                listOfChat.add(ChatUser("You",message))
                                  var adapter=MyAdapter(this@Chat_Activity,listOfChat)
                                ListViewChat.adapter=adapter

                            }
                        }catch (ex:Exception){}
                    }
                    override fun onChildRemoved(p0: DataSnapshot?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
                })



        // Recieve

        myref.child("twitter-2b40b").child(SplitString(my_email)).child("Message")
                .addChildEventListener(object :ChildEventListener{
                    var adapter_chat=GroupAdapter<ViewHolder>()

                    override fun onCancelled(p0: DatabaseError?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                        try {


                            var td = p0!!.child(SplitString(friends_email))
                            var message = td.value as String

                            if (message != null && td.key == SplitString(friends_email)) {
                                Toast.makeText(this@Chat_Activity,td.key +": send you a message",Toast.LENGTH_LONG).show()

                                listOfChat.add(ChatUser("#@+",message))
                                var adapter=MyAdapter(this@Chat_Activity,listOfChat)
                                ListViewChat.adapter=adapter

                            }
                        }catch (ex:Exception){}
                    }
                    override fun onChildRemoved(p0: DataSnapshot?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
                })
    }

    fun SplitString(email:String):String{

        var name =email.split("@")
        return name[0]
    }
    inner  class MyAdapter:BaseAdapter{
        var listOfChat:ArrayList<ChatUser>
        var context:Context?=null

        constructor(context:Context,listOfChat:ArrayList<ChatUser>){
            this.listOfChat=listOfChat
            this.context=context

        }
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var User=listOfChat[position]
            var sender_Reciever=User.who
            var inflator=context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            var CurrentView:View
            if(User.who=="You"){
                var myView=inflator.inflate(R.layout.ticket_chat_send,null)
                myView.tv_message_sent.text=User.Umessage
                CurrentView=myView
            }else{
                var myView=inflator.inflate(R.layout.ticket_chat_incomming,null)

                myView.tv_message_incomming.text=User.Umessage
                CurrentView=myView

            }

            return CurrentView
        }

        override fun getItem(position: Int): Any {
            return listOfChat[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return listOfChat.size
        }


    }



}
