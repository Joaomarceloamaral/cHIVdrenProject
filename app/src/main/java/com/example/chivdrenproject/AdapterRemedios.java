package com.example.chivdrenproject;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chivdrenproject.model.ModelMedicamento;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdapterRemedios extends RecyclerView.Adapter<AdapterRemedios.MyHolder> {
    Context context;
    List<ModelMedicamento> medicamentosList;
    String myUid;
    public AdapterRemedios(Context context, List<ModelMedicamento>medicamentosList){
        this.context = context;
        this.medicamentosList = medicamentosList;
        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_remed, parent,false);
        return new MyHolder(view);
    }
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        final String hisUID = medicamentosList.get(position).getUid();
        final String uid = medicamentosList.get(position).getUid();
        String name = medicamentosList.get(position).getName();
        final String pId = medicamentosList.get(position).getpId();
        String rNome = medicamentosList.get(position).getrNome();
        String rDescr = medicamentosList.get(position).getrDescr();
        String rHora = medicamentosList.get(position).getrHora();

        String rMin = medicamentosList.get(position).getrMin();



        String pTimeStamp= medicamentosList.get(position).getpTime();


        holder.excluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMoreOptions(holder.excluir, uid , myUid, pId);
            }
        });

        holder.editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(context, EditarActivity.class);
                intent.putExtra("pId",pId);
                context.startActivity(intent);

            }
        });

        holder.nome_remedio.setText(rNome);
        holder.descr_remedio.setText(rDescr);
        holder.hora_remedio.setText(rHora+":"+rMin);






    }



    private void showMoreOptions(ImageButton excluir, String uid, String myUid, final String pId) {
        PopupMenu popupMenu = new PopupMenu(context,excluir, Gravity.END);


        if(uid.equals(myUid)){
            popupMenu.getMenu().add(Menu.NONE,0,0,"Excluir");
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if(id == 0){
                    beginDelete(pId);
                }
                return false;
            }
        });

        popupMenu.show();

    }

    private void beginDelete(String pId) {
        delete(pId);
    }

    private void delete(final String pId) {

        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Excluindo...");

        Query fquery = FirebaseDatabase.getInstance().getReference("MeusRemedios").orderByChild("pId").equalTo(pId);
        fquery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    ds.getRef().removeValue();

                }
                Toast.makeText(context, "Excluido com Sucesso", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return medicamentosList.size();
    }


    class  MyHolder extends RecyclerView.ViewHolder{

        TextView nome_remedio,descr_remedio,hora_remedio,min_remedio;
        ImageButton excluir,editar;
        LinearLayout profile;


        public  MyHolder(@NonNull View itemView){
            super(itemView);


            nome_remedio = itemView.findViewById(R.id.RnomeTv);
            descr_remedio = itemView.findViewById(R.id.RdescrTv);
            hora_remedio = itemView.findViewById(R.id.RhoraTv);
            excluir = itemView.findViewById(R.id.excluirRem);
            editar = itemView.findViewById(R.id.editarbtn);
            profile = itemView.findViewById(R.id.layout);



        }
    }

}
