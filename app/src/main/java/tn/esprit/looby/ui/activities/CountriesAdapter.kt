package tn.esprit.looby.ui.activities

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import tn.esprit.looby.R

class CountriesAdapter(val mContext: Context,val values:Array<Country>)
    :ArrayAdapter<Country>(mContext,0,values){

    override fun getView(position: Int , convertView:View? , parent:ViewGroup): View {
        return createView(position,convertView,parent)

    }

    override fun getDropDownView(position: Int, convertView:View? , parent:ViewGroup): View {
        return createView(position,convertView,parent)

    }
    fun createView(position: Int, convertView:View? , parent:ViewGroup) : View{
        val itemView:View = LayoutInflater.from(mContext).inflate(R.layout.item_country,parent,false)

        val tvCountryName = itemView.findViewById<TextView>(R.id.tvCountryName)
        val ivCountryFlag = itemView.findViewById<ImageView>(R.id.ivCountryFlag)
        val country = values[position]

        tvCountryName.text= country.name
        ivCountryFlag.setImageResource(country.flag)

        return itemView


    }


}