package pt.hventura.asteroidradar.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import pt.hventura.asteroidradar.R
import pt.hventura.asteroidradar.databinding.DialogHelpBinding
import pt.hventura.asteroidradar.databinding.FragmentDetailBinding


class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        binding.asteroid = args.selectedAsteroid
        binding.helpButton.setOnClickListener {
            displayDialog()
        }

        return binding.root
    }

    private fun displayDialog() {
        /**
         * Possible because of this: https://github.com/afollestad/material-dialogs
         * */
        val dialog = MaterialDialog(requireContext())
        val dialogBinding = DataBindingUtil.inflate<DialogHelpBinding>(LayoutInflater.from(requireContext()), R.layout.dialog_help, (binding.root.parent) as ViewGroup, false)
        dialog.setContentView(dialogBinding.root)
        dialogBinding.dismissDialogHelp.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

}