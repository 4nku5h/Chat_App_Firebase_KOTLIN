package com.example.nikhil.twitter

import android.content.Context
import android.content.Intent
import android.icu.lang.UCharacter.GraphemeClusterBreak.V
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.ticket_users.view.*

class MainActivity : AppCompatActivity() {

    var my_email:String=""
    private var firebaseDatabase=FirebaseDatabase.getInstance()
    private var myref=firebaseDatabase.reference
    var adapter:ArrayAdapter?=null

    var listof_Users= arrayListOf<User>()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var i:MenuInflater=menuInflater
        i.inflate(R.menu.main_menu,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return super.onOptionsItemSelected(item)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Toast.makeText(this@MainActivity,"Loading Users",Toast.LENGTH_LONG).show()



        var b:Bundle=intent.extras
        my_email=b.getString("email")
        tv_my_email.text=my_email
        TotalUsers()


    }

    fun TotalUsers(){
        myref.child ("twitter-2b40b")
                .addValueEventListener(object :ValueEventListener{
                    override fun onCancelled(p0: DatabaseError?) {

                    }
                    override fun onDataChange(p0: DataSnapshot?) {
                        try{

                            listof_Users.clear()
                            val td =p0!!.value as HashMap<String,Any>
                            var name:String
                            for(key in td.keys){
                                name=key.toString()
                                if(name!=SplitString(my_email)) {
                                    listof_Users.add(User(name))
                                }
                            }

                            adapter=ArrayAdapter(this@MainActivity,listof_Users)
                            ListView_User.adapter=adapter

                        }catch (ex:Exception){}
                    }



                })

    }

    inner class ArrayAdapter:BaseAdapter{
      var listOfUser=ArrayList<User>()
         var context:Context?=null
         constructor(context:Context,listof_Users:ArrayList<User>):super(){
             this.context=context
             this.listOfUser=listof_Users
         }
         override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
         var user=listOfUser[position]
             var inflater=context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
             var myView= inflater.inflate(R.layout.ticket_users,null)
             myView.tv_User_name.text=user.name
             myView.tv_user_messge.text="#@cker's Studio"

             myView.List_Layout_to_chat_activity.setOnClickListener{

                 var i=Intent(this@MainActivity,Chat_Activity::class.java)
                 i.putExtra("name",user.name)
                 i.putExtra("friends_email",user.name+"@ankushshrivastava.com")
                 i.putExtra("my_email",my_email)
                 startActivity(i)


             }
             return myView

         }

         override fun getItem(position: Int): Any {
         return  listOfUser[position]
         }

         override fun getItemId(position: Int): Long {
          return position.toLong()
         }

         override fun getCount(): Int {
         return  listOfUser.size
         }

    }

    fun SplitString(email:String):String{

        var name =email.split("@")
        return name[0]
    }

}
