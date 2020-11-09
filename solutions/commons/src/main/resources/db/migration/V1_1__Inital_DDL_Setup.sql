SET search_path = "camel";
create table order_to_producer
(
	id uuid not null
		constraint order_to_producer_pk
			primary key,
	partner_id integer,
	item_code integer,
	item_count integer
);

alter table order_to_producer owner to camel;

create unique index order_to_producer_id_uindex
	on order_to_producer (id);