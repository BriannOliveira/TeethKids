package br.com.minhaempresa.teethkids.acceptance

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.currentRecomposeScope
import androidx.core.content.ContextCompat
import br.com.minhaempresa.teethkids.R
import br.com.minhaempresa.teethkids.databinding.FragmentMapBinding
import br.com.minhaempresa.teethkids.helper.FirebaseMyUser
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.GeoPoint


class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private val currentUser = FirebaseMyUser.getCurrentUser()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        binding.btnSendLocation.setOnClickListener {
            if(currentUser != null){
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                {
                    fusedLocationClient.lastLocation
                        .addOnSuccessListener { location: Location? ->
                            val loc = GeoPoint(location?.latitude ?: 0.0, location?.longitude ?: 0.0)
                            FirebaseFirestore.getInstance().collection("acceptances").document(currentUser.uid)
                                .update("loc",loc)

                        }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}