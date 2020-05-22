package org.example.boot.ui;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import javax.annotation.PostConstruct;
import org.example.boot.jpa.JpaPhoneRepository;
import org.example.boot.jpa.PhoneEntity;
import org.springframework.data.domain.PageRequest;

@Tag("ui-main")
@PageTitle("Main | Simple Spring Boot UI")
@Route("")
public class MainView extends VerticalLayout {

    private final JpaPhoneRepository jpaPhoneRepository;

    public MainView(JpaPhoneRepository jpaPhoneRepository) {
        this.jpaPhoneRepository = jpaPhoneRepository;
    }

    @PostConstruct
    private void init() {
        // TITLE
        var logo = new H3("Simple Spring Boot UI");
        var header = new Header(logo);

        // GRID
        var grid = new Grid<>(PhoneEntity.class, false);
        grid.setDataProvider(dataProvider());

        grid.addColumn(PhoneEntity::getId).setHeader("ID");
        grid.addColumn(PhoneEntity::getPhoneNumber).setHeader("Phone Number");
        grid.addColumn(PhoneEntity::getCreatedDateTime).setHeader("Created");


        grid.addThemeVariants(
            GridVariant.LUMO_NO_BORDER,
            GridVariant.LUMO_NO_ROW_BORDERS,
            GridVariant.LUMO_ROW_STRIPES
        );

        // ADD TO LAYOUT
        add(header, grid);
    }

    private CallbackDataProvider<PhoneEntity, Void> dataProvider() {
        return DataProvider.fromCallbacks(
                query -> jpaPhoneRepository
                    .findAll(PageRequest.of(query.getOffset(), query.getLimit()))
                    .stream(),
                query -> (int)jpaPhoneRepository.count()
            );
    }
}
