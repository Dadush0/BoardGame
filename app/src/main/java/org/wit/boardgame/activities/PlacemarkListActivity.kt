package org.wit.boardgame.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.boardgame.R
import org.wit.boardgame.activities.ui.login.LoggedInUserView
import org.wit.boardgame.activities.ui.login.LoginFragment
import org.wit.boardgame.activities.ui.login.LoginViewModel
import org.wit.boardgame.activities.ui.login.LoginViewModelFactory
import org.wit.boardgame.adapters.PlacemarkAdapter
import org.wit.boardgame.adapters.PlacemarkListener
import org.wit.boardgame.databinding.ActivityPlacemarkListBinding

import org.wit.boardgame.main.MainApp
import org.wit.boardgame.models.PlacemarkModel
var init: Boolean = true

class PlacemarkListActivity : AppCompatActivity(), PlacemarkListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityPlacemarkListBinding
    private var position: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlacemarkListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(init) {
            val fr = supportFragmentManager.beginTransaction()
            fr.replace(R.id.placemarklist, LoginFragment())
            fr.commit()
            init = !init
        }
        else {
            //setContentView(binding.root)

            binding.toolbar.title = title
            setSupportActionBar(binding.toolbar)

            app = application as MainApp

            val layoutManager = LinearLayoutManager(this)
            binding.recyclerView.layoutManager = layoutManager
            binding.recyclerView.adapter = PlacemarkAdapter(app.placemarks.findAll(), this)

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        init = !init
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                init = !init
                val launcherIntent = Intent(this, PlacemarkListActivity::class.java)
                getResult.launch(launcherIntent)
            }
            R.id.item_add -> {
                val launcherIntent = Intent(this, PlacemarkActivity::class.java)
                getResult.launch(launcherIntent)
            }
            R.id.item_map -> {
                val launcherIntent = Intent(this, PlacemarkMapsActivity::class.java)
                mapIntentLauncher.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.placemarks.findAll().size)
            }
        }

    override fun onPlacemarkClick(placemark: PlacemarkModel, position : Int) {
        val launcherIntent = Intent(this, PlacemarkActivity::class.java)
        launcherIntent.putExtra("placemark_edit", placemark)
        this.position = position
        getClickResult.launch(launcherIntent)
    }

    private val getClickResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.placemarks.findAll().size)
            }
            else // Deleting
                if (it.resultCode == 99)     (binding.recyclerView.adapter)?.notifyItemRemoved(position)
        }
    private val mapIntentLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        )    { }
}


