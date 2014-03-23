package attributes.core;

import attributes.model.*;
import org.orders.entity.Ecorescategory;
/*import org.primefaces.event.CellEditEvent;*/

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ManagedBean(name="attributesController")
@SessionScoped
public class AttributesController {
    private static Logger _log = Logger.getLogger(AttributesController.class.getName());

    private List<Ecoresattribute> ecoresattributes;
    private List<Ecoresattributegroup> ecoresattributegroups;
    private List<Ecoresattributetype> ecoresattributetypes;
    private List<Ecorescategoryattributegroup> ecorescategoryattributegroups;
    private List<Ecoresproductattributevalue> ecoresproductattributevalues;
    private List<Ecoresvalue> ecoresvalues;
    private List<Ecoresgroupbyattribute> ecoresgroupbyattributeList;
    private List<Ecoresvalueenumeration> ecoresvalueenumerationList;

    private Ecoresattribute  ecoresattribute;
    private Ecoresattributegroup ecoresattributegroup;
    private Ecoresattributetype ecoresattributetype, ecoresattributetypeSelected;
    private Ecorescategoryattributegroup ecorescategoryattributegroup;
    private Ecoresproductattributevalue ecoresproductattributevalue;
    private Ecoresvalue ecoresvalue;
    private Ecoresgroupbyattribute ecoresgroupbyattribute;
    private Ecoresvalueenumeration ecoresvalueenumeration;

    private AttributeDataTypes2 dataType;
    @EJB
    private EcoresattributeFacade attributeFacade;
    @EJB
    private EcoresattributegroupFacade attributegroupFacade;
    @EJB
    private EcoresattributetypeFacade attributetypeFacade;
    @EJB
    private EcorescategoryattributegroupFacade categoryattributegroupFacade;
    @EJB
    private EcoresproductattributevalueFacade productattributevalueFacade;
    @EJB
    private EcoresvalueFacade valueFacade;
    @EJB
    private EcoresgroupbyattributeFacade ecoresgroupbyattributeFacade;
    @EJB
    private EcoresvalueenumerationFacade ecoresvalueenumerationFacade;
    @EJB
    private GetattributeenumlistFacade getattributeenumlistFacade;


    //Переменная для множественного выбора атрибутов для привязки к группе
    private Ecoresattribute[] ecoresattributesSelection;
    //Переменная для множественного выбора группы атрибутов для привязки к иерархии категории
    private Ecoresattributegroup[] ecoresattributegroupSelection;
    //Posted by STUM
    @PostConstruct
    public void init(){
        ecoresattributes = new ArrayList<Ecoresattribute>();
        ecoresattributegroups = new ArrayList<Ecoresattributegroup>();
        ecoresattributetypes = new ArrayList<Ecoresattributetype>();
        ecorescategoryattributegroups = new ArrayList<Ecorescategoryattributegroup>();
        ecoresproductattributevalues = new ArrayList<Ecoresproductattributevalue>();
        ecoresvalues = new ArrayList<Ecoresvalue>();
        ecoresgroupbyattributeList = new ArrayList<Ecoresgroupbyattribute>();
        ecoresvalueenumerationList = new ArrayList<Ecoresvalueenumeration>();

        if(!attributeFacade.findAll().isEmpty()){ ecoresattributes = attributeFacade.findAll(); ecoresattribute = ecoresattributes.get(0);
        }else{ecoresattribute = new Ecoresattribute();}

        if(!attributegroupFacade.findAll().isEmpty()){ ecoresattributegroups = attributegroupFacade.findAll(); ecoresattributegroup = ecoresattributegroups.get(0);
        }else{ecoresattributegroup = new Ecoresattributegroup();}

        if(!attributetypeFacade.findAll().isEmpty()){ ecoresattributetypes = attributetypeFacade.findAll(); ecoresattributetype = ecoresattributetypes.get(0);
        }else{ecoresattributetype = new Ecoresattributetype();}

        if(!categoryattributegroupFacade.findAll().isEmpty()){ ecorescategoryattributegroups = categoryattributegroupFacade.findAll(); ecorescategoryattributegroup = ecorescategoryattributegroups.get(0);
        }else{ecorescategoryattributegroup = new Ecorescategoryattributegroup();}

        if(!productattributevalueFacade.findAll().isEmpty()){ ecoresproductattributevalues = productattributevalueFacade.findAll(); ecoresproductattributevalue = ecoresproductattributevalues.get(0);
        }else{ecoresproductattributevalue = new Ecoresproductattributevalue();}

        if(!valueFacade.findAll().isEmpty()){ ecoresvalues = valueFacade.findAll(); ecoresvalue = ecoresvalues.get(0);
        }else{ecoresvalue = new Ecoresvalue();}

        if(!ecoresgroupbyattributeFacade.findAll().isEmpty()){ ecoresgroupbyattributeList = ecoresgroupbyattributeFacade.findAll(); ecoresgroupbyattribute = ecoresgroupbyattributeList.get(0);
        }else{ecoresgroupbyattribute = new Ecoresgroupbyattribute();}

        if(!ecoresvalueenumerationFacade.findAll().isEmpty()){ ecoresvalueenumerationList = ecoresvalueenumerationFacade.findAll(); ecoresvalueenumeration = ecoresvalueenumerationList.get(0);
        }else{ecoresvalueenumeration = new Ecoresvalueenumeration();}

    }

    //Сохранение типа данных для ТИПА АТРИБУТА
    public void saveAttributeDataType(){
        ecoresattributetype.setDataType(dataType.getCode().toString());
        attributetypeFacade.save(ecoresattributetype);
        refreshEcoresattributetypes();
        ecoresattributetype = attributetypeFacade.find(ecoresattributetype.getRecid());
        addMessage("Тип данных изменен на: " + dataType.getCode());
    }

    public SelectItem[] getAttributeDataTypeValues2() {
        SelectItem[] items = new SelectItem[AttributeDataTypes2.values().length];
        int i = 0;
        for(AttributeDataTypes2 g: AttributeDataTypes2.values()) {
            items[i++] = new SelectItem(g, g.getLabel().toString());
        }
        return items;
    }

    public AttributeDataTypes2 getDataType() {
        return dataType;
    }

    public void setDataType(AttributeDataTypes2 dataType) {
        this.dataType = dataType;
    }

    //Получение имени Атрибута
        public String getAttributeName(Long recid){
            return ecoresgroupbyattributeFacade.findAttribute(recid).getAttributeName();
        }
    //Получение имени Группы Атрибутов
        public String getAttributeGroupName(Long recid){
            return attributegroupFacade.find(recid).getName();
        }
    //Получение имени Типа Атрибута
    public String getAttributeTypeName(Long recid){
        return attributetypeFacade.find(recid).getTypeName();
    }

    public Ecoresattributetype getAttributeType(Long recid){
        Ecoresattributetype ecoresattributetypeCur = new Ecoresattributetype();
        ecoresattributetypeCur = attributetypeFacade.find(recid);
        return ecoresattributetypeCur;
    }
    //Обновление табличной части
        public void refreshEcoresattributegroups(){
            ecoresattributegroups = attributegroupFacade.findAll();
        }
        public void refreshEcoresattributes(){
            ecoresattributes = attributeFacade.findAll();
        }
        public void refreshEcoresgroupbyattribute(){
            ecoresgroupbyattributeList = ecoresgroupbyattributeFacade.findAttributesByGroup(ecoresattributegroup);
        }
        public void refreshEcoresattributetypes(){
            ecoresattributetypes = attributetypeFacade.findAll();
        }
        public void refreshEcoresvalueenumerationList(){
            ecoresvalueenumerationList = ecoresvalueenumerationFacade.findTypeEnumerationList(ecoresattributetype);
        }
        public void refreshAttributeGroupCategory(Ecorescategory ecorescategory){
            ecorescategoryattributegroups = categoryattributegroupFacade.findAttributeGroupsByCategory(ecorescategory);
        }

    //Привязка атрибута к группе и удаление

        public void addAttributeToGroup(){
            ecoresgroupbyattributeFacade.attachAttributesToGroup(ecoresattributesSelection, ecoresattributegroup);
            addMessage("Атрибуты привязаны к группе!");
        }
        public void deleteAttributeToGroup(){
            ecoresgroupbyattributeFacade.remove(ecoresgroupbyattribute);
            refreshEcoresgroupbyattribute();
        }
        public void selectAttributesByGroup(){
            ecoresgroupbyattributeList.clear();
            ecoresgroupbyattributeList = ecoresgroupbyattributeFacade.findAttributesByGroup(ecoresattributegroup);
            ecoresattributesSelection = new Ecoresattribute[attributeFacade.findAll().size()];
            addMessage(ecoresattributegroup.getName());
        }

    //Привязка Группы атрибутов к иерархии категорий

    public void addAttributeGroupCategory(){

        addMessage("Атрибуты привязаны к группе!");
    }
    public void addAttributeGroupSelection(Ecorescategory ecorescategory){
        for(Ecoresattributegroup attributegroup : ecoresattributegroupSelection){
            _log.info("Выбранные группы: " + attributegroup.getName());
            categoryattributegroupFacade.create(attributegroup, ecorescategory);
        }
        ecoresattributegroupSelection = new Ecoresattributegroup[attributegroupFacade.findAll().size()];
        refreshAttributeGroupCategory(ecorescategory);
    }

    public void deleteAttributeGroupCategory(Ecorescategory ecorescategory){
        categoryattributegroupFacade.delete(ecorescategoryattributegroup);
        refreshAttributeGroupCategory(ecorescategory);
    }
    public void selectAttributeGroupCategory(){
        addMessage(ecorescategoryattributegroup.getAttributeGroupRef().toString());
    }

    //CRUD команды для записей в табличной части Атрибутов
            public void createEcoresattribute(){
                attributeFacade.createEcoresattribute(ecoresattributegroup);
                refreshEcoresattributes();
            }
            public void deleteEcoresattribute(){
                attributeFacade.delete(ecoresattribute);
                refreshEcoresattributes();
            }
            public void saveEcoresattribute(){
                attributeFacade.save(ecoresattribute);
                ecoresattribute = attributeFacade.find(ecoresattribute.getRecid());
            }
            public void setAttributeType(){
                ecoresattribute.setAttributeTypeRef(ecoresattributetypeSelected.getRecid());
                _log.info("Атрибуту установлен типа данных: " + ecoresattributetypeSelected.getRecid());

                saveEcoresattribute();
            }
            public void selectionAttribute(){
                ecoresattributetypeSelected = attributetypeFacade.find(ecoresattribute.getAttributeTypeRef());
                for(Getattributeenumlist enumlist : getattributeenumlistFacade.findEnumsByAttributeId(ecoresattribute)){
                    _log.info("Значение атрибута ПРОВЕРКА 222-888-55: " + enumlist.getTextValue());
                }
                addMessage("Атрибут выбран" + ecoresattribute.getRecid() + ", тип: " + ecoresattribute.getAttributeTypeRef());
            }

    //CRUD команды для записей в табличной части Группы Атрибутов
        public void createEcoresattributegroup(){
            attributegroupFacade.create();
            refreshEcoresattributegroups();
        }
        public void saveEcoresattributegroup(){
            attributegroupFacade.save(ecoresattributegroup);
        }
        public void deleteEcoresattributegroup(){
            ecoresattributegroup = attributegroupFacade.find(ecoresattributegroup.getRecid());
            attributegroupFacade.delete(ecoresattributegroup);
            refreshEcoresattributegroups();
            ecoresattributegroup = ecoresattributegroups.get(0);
        }
    //CRUD команды для записей в табличной части Типы Атрибутов
        public void createEcoresattributeType(){
            ecoresattributetype = attributetypeFacade.create();
            refreshEcoresattributetypes();
        }
        public void saveEcoresattributeType(){
            attributetypeFacade.save(ecoresattributetype);
            ecoresattributetype = attributetypeFacade.find(ecoresattributetype.getRecid());
            addMessage("Сохранено");
        }
        public void deleteEcoresattributeType(){
            ecoresattributetype = attributetypeFacade.find(ecoresattributetype.getRecid());
            attributetypeFacade.delete(ecoresattributetype);
            refreshEcoresattributetypes();
            ecoresattributetype = ecoresattributetypes.get(0);
        }
        public void checkboxChanged(Boolean chkbox){
            attributetypeFacade.save(ecoresattributetype);
            ecoresattributetype = attributetypeFacade.find(ecoresattributetype.getRecid());
            addMessage(ecoresattributetype.getEnumeration().toString());
        }
        //Выбор типа атрибута на гриде
        public void selectAttributeType(){
            ecoresvalueenumerationList = ecoresvalueenumerationFacade.findTypeEnumerationList(ecoresattributetype);
            dataType = AttributeDataTypes2.getAttributeDataTypes2(ecoresattributetype.getDataType());
            addMessage("Код типа: " + ecoresattributetype.getDataType() +
                            ", dataType: " + AttributeDataTypes2.getAttributeDataTypes2(ecoresattributetype.getDataType()));
        }
        public void addTypeValue(){
            ecoresvalueenumerationFacade.create(ecoresattributetype);
            refreshEcoresvalueenumerationList();
        }
        public void deleteTypeValue(){
            ecoresvalueenumerationFacade.delete(ecoresvalueenumeration);
            refreshEcoresvalueenumerationList();
        }
        public void saveTypeValue(){
            ecoresvalueenumerationFacade.save(ecoresvalueenumeration);
            _log.info("Значение списка: " + ecoresvalueenumeration.getTextValue());
            ecoresvalueenumeration =  ecoresvalueenumerationFacade.find(ecoresvalueenumeration.getRecid());
            refreshEcoresvalueenumerationList();
            addMessage("Значение сохранено");
        }

        /*public void onCellEdit(CellEditEvent event) {
            Object oldValue = event.getOldValue();
            Object newValue = event.getNewValue();

            if(newValue != null && !newValue.equals(oldValue)) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }

            saveTypeValue();
        }*/
    //Свойства отображения на странице
    public Ecoresvalue getEcoresvalue() {
        return ecoresvalue;
    }

    public void setEcoresvalue(Ecoresvalue ecoresvalue) {
        this.ecoresvalue = ecoresvalue;
    }

    public Ecoresproductattributevalue getEcoresproductattributevalue() {
        return ecoresproductattributevalue;
    }

    public void setEcoresproductattributevalue(Ecoresproductattributevalue ecoresproductattributevalue) {
        this.ecoresproductattributevalue = ecoresproductattributevalue;
    }

    public Ecorescategoryattributegroup getEcorescategoryattributegroup() {
        return ecorescategoryattributegroup;
    }

    public void setEcorescategoryattributegroup(Ecorescategoryattributegroup ecorescategoryattributegroup) {
        this.ecorescategoryattributegroup = ecorescategoryattributegroup;
    }

    public Ecoresattributetype getEcoresattributetype() {
        return ecoresattributetype;
    }

    public void setEcoresattributetype(Ecoresattributetype ecoresattributetype) {
        this.ecoresattributetype = ecoresattributetype;
    }

    public Ecoresattributegroup getEcoresattributegroup() {
        return ecoresattributegroup;
    }

    public void setEcoresattributegroup(Ecoresattributegroup ecoresattributegroup) {
        this.ecoresattributegroup = ecoresattributegroup;
    }

    public Ecoresattribute getEcoresattribute() {
        return ecoresattribute;
    }

    public void setEcoresattribute(Ecoresattribute ecoresattribute) {
        this.ecoresattribute = ecoresattribute;
    }

    public List<Ecoresvalue> getEcoresvalues() {
        return ecoresvalues;
    }

    public void setEcoresvalues(List<Ecoresvalue> ecoresvalues) {
        this.ecoresvalues = ecoresvalues;
    }

    public List<Ecoresproductattributevalue> getEcoresproductattributevalues() {
        return ecoresproductattributevalues;
    }

    public void setEcoresproductattributevalues(List<Ecoresproductattributevalue> ecoresproductattributevalues) {
        this.ecoresproductattributevalues = ecoresproductattributevalues;
    }

    public List<Ecorescategoryattributegroup> getEcorescategoryattributegroups() {
        return ecorescategoryattributegroups;
    }

    public void setEcorescategoryattributegroups(List<Ecorescategoryattributegroup> ecorescategoryattributegroups) {
        this.ecorescategoryattributegroups = ecorescategoryattributegroups;
    }

    public List<Ecoresattributetype> getEcoresattributetypes() {
        return ecoresattributetypes;
    }

    public void setEcoresattributetypes(List<Ecoresattributetype> ecoresattributetypes) {
        this.ecoresattributetypes = ecoresattributetypes;
    }

    public List<Ecoresattributegroup> getEcoresattributegroups() {
        return ecoresattributegroups;
    }

    public void setEcoresattributegroups(List<Ecoresattributegroup> ecoresattributegroups) {
        this.ecoresattributegroups = ecoresattributegroups;
    }

    public List<Ecoresattribute> getEcoresattributes() {
        return ecoresattributes;
    }

    public void setEcoresattributes(List<Ecoresattribute> ecoresattributes) {
        this.ecoresattributes = ecoresattributes;
    }

    public Ecoresgroupbyattribute getEcoresgroupbyattribute() {
        return ecoresgroupbyattribute;
    }

    public void setEcoresgroupbyattribute(Ecoresgroupbyattribute ecoresgroupbyattribute) {
        this.ecoresgroupbyattribute = ecoresgroupbyattribute;
    }

    public List<Ecoresgroupbyattribute> getEcoresgroupbyattributeList() {
        return ecoresgroupbyattributeList;
    }

    public void setEcoresgroupbyattributeList(List<Ecoresgroupbyattribute> ecoresgroupbyattributeList) {
        this.ecoresgroupbyattributeList = ecoresgroupbyattributeList;
    }

    public Ecoresattribute[] getEcoresattributesSelection() {
        return ecoresattributesSelection;
    }

    public void setEcoresattributesSelection(Ecoresattribute[] ecoresattributesSelection) {
        this.ecoresattributesSelection = ecoresattributesSelection;
    }

    public List<Ecoresvalueenumeration> getEcoresvalueenumerationList() {
        return ecoresvalueenumerationList;
    }

    public void setEcoresvalueenumerationList(List<Ecoresvalueenumeration> ecoresvalueenumerationList) {
        this.ecoresvalueenumerationList = ecoresvalueenumerationList;
    }

    public Ecoresvalueenumeration getEcoresvalueenumeration() {
        return ecoresvalueenumeration;
    }

    public void setEcoresvalueenumeration(Ecoresvalueenumeration ecoresvalueenumeration) {
        this.ecoresvalueenumeration = ecoresvalueenumeration;
    }

    public Ecoresattributegroup[] getEcoresattributegroupSelection() {
        return ecoresattributegroupSelection;
    }

    public void setEcoresattributegroupSelection(Ecoresattributegroup[] ecoresattributegroupSelection) {
        this.ecoresattributegroupSelection = ecoresattributegroupSelection;
    }

    public Ecoresattributetype getEcoresattributetypeSelected() {
        return ecoresattributetypeSelected;
    }

    public void setEcoresattributetypeSelected(Ecoresattributetype ecoresattributetypeSelected) {
        this.ecoresattributetypeSelected = ecoresattributetypeSelected;
    }

    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
