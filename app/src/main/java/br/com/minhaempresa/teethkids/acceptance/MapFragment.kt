package br.com.minhaempresa.teethkids.acceptance

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import br.com.minhaempresa.teethkids.R
import br.com.minhaempresa.teethkids.databinding.FragmentMapBinding
import br.com.minhaempresa.teethkids.helper.FirebaseMyUser
import br.com.minhaempresa.teethkids.menu.emergency.recyclerViewEmergencies.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ListenerRegistration

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private val currentUser = FirebaseMyUser.getCurrentUser()
    private lateinit var uidRescuer: String
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var listener: ListenerRegistration

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        binding.mapView.onCreate(savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        //configurando mapView
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            binding.mapView.getMapAsync { googleMap ->
                map = googleMap
                enableUserLocation()
            }
        }

        //pegar o uid do socorrista
        if(currentUser != null){
            FirebaseFirestore.getInstance().collection("acceptances").document(currentUser.uid).get()
                .addOnSuccessListener { document ->
                    if(document != null){
                        uidRescuer = document.getString("uid_usuario")!!
                    }
                }
        }

        //mandar localização
        binding.btnSendLocation.setOnClickListener {
            binding.pbSendLocation.visibility = View.VISIBLE
            if(currentUser != null){
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                {
                    fusedLocationClient.lastLocation
                        .addOnSuccessListener { location: Location? ->
                            val loc = GeoPoint(location?.latitude ?: 0.0, location?.longitude ?: 0.0)
                            FirebaseFirestore.getInstance().collection("acceptances").document(currentUser.uid)
                                .update("loc",loc)
                            binding.pbSendLocation.visibility = View.GONE
                            binding.btnFinishEmergency.visibility = View.VISIBLE
                            binding.btnSendLocation.visibility = View.GONE
                        }
                }
            }
        }

        if (currentUser != null){
            //ouvir para o caso o socorrista já tenho encerrado a emergência
            listener = FirebaseFirestore.getInstance().collection("acceptances").document(currentUser.uid)
                .addSnapshotListener{snapshot, e ->
                if(e != null){
                    Toast.makeText(requireContext(),"Houve algum erro. Por favor tente novamente!", Toast.LENGTH_LONG).show()
                    //mudar para fragment anterior
                    return@addSnapshotListener
                }
                //verificar se o documento foi excluído
                if(snapshot != null && !snapshot.exists()){
                    listener.remove()
                    val emergencyDoneFragment = EmergencyDoneFragment()
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.container_acceptance,emergencyDoneFragment)
                        .commit()
                }
            }
        }

        //finalizar a emergência
        binding.btnFinishEmergency.setOnClickListener {
            FirebaseFirestore.getInstance().collection("emergencies").document(uidRescuer)
                .update("status",Status.done).addOnSuccessListener {
                    if(currentUser != null){
                        FirebaseFirestore.getInstance().collection("acceptances").document(currentUser.uid)
                            .delete().addOnSuccessListener {
                                val emergencyDoneFragment = EmergencyDoneFragment()
                                requireActivity().supportFragmentManager.beginTransaction()
                                    .replace(R.id.container_acceptance,emergencyDoneFragment)
                                    .commit()
                            }
                    }
                }
        }
    }

    //função para obter a localização para o MapView
    @SuppressLint("MissingPermission")
    private fun enableUserLocation(){
        map.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val latLng = com.google.android.gms.maps.model.LatLng(it.latitude, it.longitude)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15f))
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        binding.mapView.onPause()
        super.onPause()
    }

    override fun onStop() {
        binding.mapView.onStop()
        super.onStop()
    }

    override fun onLowMemory() {
        binding.mapView.onLowMemory()
        super.onLowMemory()
    }

    override fun onDestroyView() {
        binding.mapView.onDestroy()
        super.onDestroyView()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

}