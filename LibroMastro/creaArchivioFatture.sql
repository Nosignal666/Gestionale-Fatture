--a cura dell'admin di settare i privilegi affinchè 
--le colonne utente non siano inseribili tramite un INSERT ne modificabili tramite un UPDATE
--le colonne stato,numeroProgressivo di fatture non siano inseribili tramite un INSERT ne modificabili con UPDATE
--la colonna indicePagamento di PP non sia inseribile tramite un INSERT ne modificabile tramite un UPDATE


create table aziende(
	nomeAzienda varchar(50) constraint valid_nomeAzienda check(nomeAzienda!='') not null,
	partitaIva char(11),
	unique (partitaiva),
	utente varchar(50) default current_user,
	unique (nomeAzienda,partitaIva)
);

create type statopagamento as enum('completato','parziale','non pagata');
create table fatture(
	partitaIva varchar(11) not null ,
	nomeAzienda varchar(50) not null,
	numeroProgressivo bigserial not null,
	codiceunivoco varchar(20) not null constraint valid_codiceunivoco check(codiceunivoco!=''),
	dataEmissione date not null,
	dataScadenza date not null constraint valid_datascadenza check(datascadenza>=dataemissione),
	importo decimal(12,2) not null constraint valid_importo check(importo >=0),
	stato statopagamento default 'non pagata',
	primary key (codiceunivoco,partitaiva),
	note varchar(1000),
	utente varchar(100) default current_user,
	pdfFile bytea,
	foreign key (partitaIva,nomeAzienda) references aziende(partitaIva,nomeAzienda) on update cascade,
	unique (numeroProgressivo)
);

create type tipoPagamento as enum('Bonifico','Contanti');

create table pagamentiParziali(
	partitaiva varchar(11) not null,
	indicepagamento bigserial not null,
	unique(indicepagamento),
	codiceunivoco varchar(20) not null,
	dataPagamento date not null,
	importo decimal(12,2) constraint valid_importo check(importo>=0) not null,
	tipoPagamento tipoPagamento not null,
	utente varchar(100) check(utente!='') default current_user,
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
case tg_op
when 'INSERT','UPDATE' then
if dataemissione>new.datapagamento from fatture where (codiceunivoco,partitaiva)=(new.codiceunivoco,new.partitaiva) then
raise exception 'la data di pagamento è antecedente alla data di emissione della fattura';
elsif sum(pagamentiparziali.importo)>fatture.importo from pagamentiparziali,fatture where (pagamentiparziali.codiceunivoco,pagamentiparziali.partitaiva)
=(new.codiceunivoco,new.partitaiva) 
and (fatture.codiceunivoco, fatture.partitaiva)=(new.codiceunivoco,new.partitaiva)
group by (fatture.codiceunivoco,fatture.partitaiva) then raise exception 'importo eccede ammontare rimanente della fattura';
elsif sum(pagamentiparziali.importo)<fatture.importo from pagamentiparziali,fatture where (pagamentiparziali.codiceunivoco,pagamentiparziali.partitaiva)
=(new.codiceunivoco,new.partitaiva) 
and (fatture.codiceunivoco, fatture.partitaiva)=(new.codiceunivoco,new.partitaiva)
group by (fatture.codiceunivoco,fatture.partitaiva) then update fatture set stato='parziale'
where (fatture.codiceunivoco,fatture.partitaiva)=(new.codiceunivoco,new.partitaiva);
else update fatture set stato='completato' where (fatture.codiceunivoco,fatture.partitaiva)=(new.codiceunivoco,new.partitaiva);
new.utente=current_user;
end if;
when 'DELETE' then
if count(*)=0 from pagamentiparziali where (codiceunivoco,partitaiva)=(new.codiceunivoco,new.partitaiva) then
update fatture set stato='non pagata';
else update fatture set stato="parziale" where (codiceunivoco,partitaiva)=(new.codiceunivoco,new.partitaiva);
end if;
return new;
end case;
return new;
end;
$$ language plpgsql;

create trigger checkCoerencePPTrigger 
after insert or delete or update on pagamentiParziali 
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
new.utente=current_user;
end if;
return new;
end;
$$ language plpgsql;

create trigger checkCoerenceFTrigger 
after update on fatture
for each row
execute procedure checkCoerenceF();



