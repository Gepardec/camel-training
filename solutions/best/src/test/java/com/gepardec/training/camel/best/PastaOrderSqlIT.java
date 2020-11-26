package com.gepardec.training.camel.best;

import com.gepardec.training.camel.best.db.CommonOperations;
import com.gepardec.training.camel.commons.config.DbConnection;
import com.gepardec.training.camel.commons.domain.OrderItem;
import com.gepardec.training.camel.commons.domain.OrderToProducer;
import com.gepardec.training.camel.commons.test.routetest.CamelRouteCDITest;
import com.gepardec.training.camel.commons.test.routetest.MockableEndpoint;
import com.gepardec.training.camel.commons.test.routetest.RouteId;
import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.cdi.Uri;
import org.apache.camel.test.cdi.Beans;
import org.apache.camel.test.cdi.CamelCdiRunner;
import org.assertj.db.type.Table;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

import static com.ninja_squad.dbsetup.operation.CompositeOperation.sequenceOf;
import static org.assertj.db.api.Assertions.assertThat;

@RunWith(CamelCdiRunner.class)
@Beans(classes = PastaOrderRouteBuilder.class)
@Ignore("Run only if main app is not started")
public class PastaOrderSqlIT extends CamelRouteCDITest {

    @Inject
    @Uri("direct:triggerSql")
    @MockableEndpoint(PastaOrderRouteBuilder.ENTRY_SEDA_ENDOINT_URI)
    @RouteId(PastaOrderRouteBuilder.ENTRY_SEDA_ENDOINT_URI)
    private ProducerTemplate entryEndpoint;

    @Test
    public void correctInput_CorrectEntryIsCreated() throws IOException, SQLException {
        clearDB();

        OrderToProducer orderToProducer = new OrderToProducer(new OrderItem(OrderItem.PASTA, 120), 2);
        entryEndpoint.sendBody(orderToProducer);

        Table table = new Table(DbConnection.getDatasource(), "order_to_producer");

        assertThat(table).hasNumberOfRows(1)
                .column("partner_id").value().isEqualTo(2)
                .column("item_code").value().isEqualTo(2)
                .column("item_count").value().isEqualTo(120);
    }

    private void clearDB() throws IOException, SQLException {
        Operation operation =
                sequenceOf(
                        CommonOperations.DELETE_ALL);
        DataSource dataSource = DbConnection.getDatasource();
        DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), operation);
        dbSetup.launch();

    }


}