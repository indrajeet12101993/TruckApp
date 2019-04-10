package com.truckintransit.operator.fragment.driverEdit


import android.os.Bundle
import android.app.Fragment
import android.view.*
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction

import com.truckintransit.operator.R
import com.truckintransit.operator.base.BaseFragment
import com.truckintransit.operator.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_add_driver_basic_info_two.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class AddDriverBasicInfoTwoEditFragment : BaseFragment() {
    private lateinit var mSharedViewModel: SharedViewModel
    private lateinit var mIsMenuShow: String
    private lateinit var driver_photo: TextView
    private lateinit var driver_adahar_photo: TextView
    private lateinit var driver_dl_photo: TextView
    private lateinit var driver_current_adress: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_add_driver_basic_info_two, container, false)





        driver_photo = view.driver_photo
        driver_adahar_photo = view.driver_adahar_photo
        driver_dl_photo = view.driver_dl_photo
        driver_current_adress = view.driver_current_adress
        driver_photo.text = getString(R.string.edit_driver_photo)
        driver_adahar_photo.text = getString(R.string.edit_adhar_photo)
        driver_dl_photo.text = getString(R.string.edit_dl_photo)
        driver_current_adress.text = getString(R.string.edit_current_adress_photo)
        view.driver_photo.setOnClickListener {
            replaceFragment(AddDriverPhotoEditFragment())

        }
        view.driver_adahar_photo.setOnClickListener {
            replaceFragment(AddDriverAdharPhotoEditFragment())
        }
        view.driver_dl_photo.setOnClickListener {

            replaceFragment(AddDriverDlPhotoEditFragment())
        }
        view.driver_current_adress.setOnClickListener {

            replaceFragment(AddDriverAdressEditFragment())
        }
        return view
    }

    private fun replaceFragment(fragment: androidx.fragment.app.Fragment) {

        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.place_holder_for_fragment, fragment)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun transfer(item: String?) {
        mIsMenuShow = item!!

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {


    }


}
