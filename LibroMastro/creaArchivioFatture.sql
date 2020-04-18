--a cura dell'admin di settare i privilegi affinchè 
--le colonne utente non siano inseribili tramite un INSERT ne modificabili tramite un UPDATE
--le colonne stato,numeroProgressivo di fatture non siano inseribili tramite un INSERT ne modificabili con UPDATE
--la colonna indicePagamento di PP non sia inseribile tramite un INSERT ne modificabile tramite un UPDATE


create table aziende(
	nomeAzienda varchar(50) constraint valid_nomeAzienda check(nomeAzienda!='') not null,
	indiceOggetto serial,
	partitaIva char(11),
	unique (partitaiva),
	unique (nomeAzienda,partitaIva)
);

create table fatture(
	partitaIva varchar(11) not null ,
	nomeAzienda varchar(50) not null,
	indiceOggetto bigserial not null,
	unique(indiceOggetto),
	codiceunivoco varchar(20) not null constraint valid_codiceunivoco check(codiceunivoco!=''),
	dataEmissione date not null,
	dataScadenza date not null constraint valid_datascadenza check(datascadenza>=dataemissione),
	importo decimal(12,2) not null constraint valid_importo check(importo >0),
	primary key (codiceunivoco,partitaiva),
	note varchar(1000),
	pdfFile bytea,
	foreign key (partitaIva,nomeAzienda) references aziende(partitaIva,nomeAzienda) on update cascade
);

create type tipoPagamento as enum('Bonifico','Contanti');

create table pagamentiParziali(
	partitaiva varchar(11) not null,
	indiceOggetto bigserial not null,
	unique(indiceOggetto),
	codiceunivoco varchar(20) not null,
	dataPagamento date not null,
	importo decimal(12,2) constraint valid_importo check(importo>0) not null,
	tipoPagamento tipoPagamento not null,
	foreign key (codiceunivoco,partitaiva) references fatture(codiceunivoco,partitaiva) on delete cascade on update cascade
	
);

--queste trigger functions assicurano che il pagamento parziale sia coerente con i dati nella fattura, cioè verifica
--che la data di pagamento non sia antecedente alla datta di emissione della fattura, e che l'importo sia tale che
--sommato agli altri importi dei pagamenti parziali fratelli, non dia un valore maggiore dell'importo della fattura.

--inoltre quando si cambia il campo importo della fattura, il sistema solleva l'eccezione quando la somma degli
--importi dei pagamenti parziali relativi a quella fattura eccede il nuovo importo della fattura, chiedendo all'utente
--di modificare o cancellare i pagamenti parziali prima;
create function checkCoerencePP() returns trigger as
$$
begin
if dataemissione>new.datapagamento from fatture where (codiceunivoco,partitaiva)=(new.codiceunivoco,new.partitaiva) then
raise exception 'la data di pagamento è antecedente alla data di emissione della fattura';
elsif sum(pagamentiparziali.importo)>fatture.importo from pagamentiparziali,fatture where (pagamentiparziali.codiceunivoco,pagamentiparziali.partitaiva)
=(new.codiceunivoco,new.partitaiva) 
and (fatture.codiceunivoco, fatture.partitaiva)=(new.codiceunivoco,new.partitaiva)
group by (fatture.codiceunivoco,fatture.partitaiva) then raise exception 'importo eccede ammontare rimanente della fattura';
end if;
return new;
end;
$$ language plpgsql security definer;

create trigger checkCoerencePPTrigger 
before insert or delete or update on pagamentiParziali 
for each row
execute procedure checkCoerencePP();


create function checkCoerenceF() returns trigger as
$$
begin
if sum(pagamentiparziali.importo)>new.importo from pagamentiparziali
where pagamentiparziali.codiceunivoco=new.codiceunivoco then
raise exception 'somma dei pagamenti parziali eccedente nuovo importo fattura,modificare o cancellare prima 
i pagamenti parziali';
elsif min(pagamentiparziali.dataPagamento)<new.dataEmissione from pagamentiparziali where pagamentiparziali.codiceunivoco=new.codiceunivoco 
then raise exception 'almeno un pagamento parziale ha la data pagamento antecedente la nuova data di emissione,
modificare o cancellare prima i pagamenti parziali' ;
end if;
return new;
end;
$$ language plpgsql security definer;

create trigger checkCoerenceFTrigger 
before update on fatture
for each row
execute procedure checkCoerenceF();


--Tabelle per sistema di tracciamento operazioni
create extension hstore;
create table operazionieffettuate(
	id serial,
	indiceOggetto int,
	tstamp timestamp default now(),
	nometabella text,
	operazione text,
	utente text default session_user,
	new_val hstore,
	old_val hstore
);
	
create function register_op() returns trigger as $$
begin
case tg_op
when 'INSERT' then
insert into operazionieffettuate(indiceOggetto,nometabella,operazione,new_val) values(new.indiceOggetto,tg_table_name,tg_op,hstore(new));
return new;
when 'UPDATE' then
insert into operazionieffettuate(indiceOggetto,nometabella,operazione,new_val,old_val) values(new.indiceOggetto,tg_table_name,tg_op,hstore(new),hstore(old));
return new;
when 'DELETE' then
insert into operazionieffettuate(indiceOggetto,nometabella,operazione,old_val) values(old.indiceOggetto,tg_table_name,tg_op,hstore(old));
return old;
end case;
end;
$$ language plpgsql security definer;

create trigger register_opTrigger 
after insert or delete or update on aziende 
for each row
execute procedure register_op();

create trigger register_opTrigger 
after insert or delete or update on fatture
for each row
execute procedure register_op();

create trigger register_opTrigger 
after insert or delete or update on pagamentiparziali
for each row
execute procedure register_op();
