package com.example.nikhil.twitter

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.style.UpdateAppearance
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_signup.*
import android.app.Activity



class signup : AppCompatActivity() {

    private var firebeseDatabase= FirebaseDatabase.getInstance()
    private var myref=firebeseDatabase.reference
    private var mAuth:FirebaseAuth?=null
    var register_state =0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        mAuth=FirebaseAuth.getInstance()
        tv_my_name.text="@ankushshrivastava.com"
        Toast.makeText(this@signup," #@ckers Studio App By \n Ankush Shrivastava",Toast.LENGTH_LONG).show()
        btn_image.setOnClickListener(View.OnClickListener {

            Toast.makeText(this@signup,"this feature will be in next version",Toast.LENGTH_LONG).show()

        })


    }

    fun btnn_register_event(view: View){

           if(register_state==0){
               register_state=1
               Login_to_firebase(et_name.text.toString()+"@ankushshrivastava.com",et_pass.text.toString())
               button_register.isClickable=false
           }



    }
    fun Login_to_firebase(email:String,password:String){
        mAuth!!.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this@signup){ task->

                    if(task.isSuccessful){
                        Toast.makeText(this@signup,"Logged in successfully",Toast.LENGTH_SHORT).show()
                      LoadMain()
                    }
                    else{
                        register_state=0
                        button_register.isClickable=true
                        Toast.makeText(this@signup,"Login Faild, Try Long password or check your internet connection",Toast.LENGTH_LONG).show()

                    }

                }

    }
    fun LoadMain(){
            var currentUser=mAuth!!.currentUser
        if(currentUser!=null){
            myref.child ("twitter-2b40b").child(SplitString(currentUser.email.toString())).setValue(currentUser.uid)
            var i=Intent(this@signup,MainActivity::class.java)
            i.putExtra("email",currentUser.email)
            startActivity(i)
            finish()
        }
    }
    fun SplitString(email:String):String{

         var name =email.split("@")
        return name[0]
    }
    override fun onStart() {
        super.onStart()
        LoadMain()
    }
}
