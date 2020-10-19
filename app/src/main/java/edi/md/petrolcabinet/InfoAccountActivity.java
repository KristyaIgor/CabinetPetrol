package edi.md.petrolcabinet;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edi.md.petrolcabinet.realm.objects.Accounts;
import edi.md.petrolcabinet.realm.objects.Company;
import edi.md.petrolcabinet.remote.client.ContractInClient;

public class InfoAccountActivity extends AppCompatActivity {
    Accounts client;
    @BindView(R.id.text_client_name) TextView textClientName;
    @BindView(R.id.text_other_info) TextView textOtherInfo;
    @BindView(R.id.image_back_information_activity) ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_info_account);
        ButterKnife.bind(this);

        Company company = BaseApp.getAppInstance().getCompanyClicked();
        client = BaseApp.getAppInstance().getClientClicked();

        textClientName.setText(client.getName());
        List<ContractInClient> contracts = client.getContracts();
        String forNumber = "un contract";
        StringBuilder forCode = new StringBuilder();

        if(contracts.size() > 1){
            forNumber = contracts.size() + " contracte";
            for (ContractInClient contract : contracts){
                forCode.append(contract.getCode()).append("\n");
            }
        }
        else{
            forCode.append(contracts.get(0).getCode());
        }

        StringBuilder info = new StringBuilder();
        if(client.getTypeClient() == 1){
            info.append("   Persoana juridica cu IDNP ")
                    .append(client.getIDNP())
                    .append(" este client a companiei ")
                    .append(company.getName())
                    .append(".")
                    .append(client.getName())
                    .append(" a incheiat ")
                    .append(forNumber)
                    .append(": ")
                    .append(forCode.toString())
                    .append(".");
        }
        else{
            info.append("   Persoana fizic  ")
                    .append(client.getName())
                    .append(" este client a companiei ")
                    .append(company.getName())
                    .append(".")
                    .append(client.getName())
                    .append(" a incheiat ")
                    .append(forNumber)
                    .append(": ")
                    .append(forCode.toString())
                    .append(".");
        }

        textOtherInfo.setText(info.toString());

        btnBack.setOnClickListener(view -> {
            finish();
        });
    }
}