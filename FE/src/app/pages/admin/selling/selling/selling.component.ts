import {Component, OnDestroy, OnInit, TemplateRef, ViewChild, ViewEncapsulation} from '@angular/core';
import {FormControl, NgModel} from '@angular/forms';
import {SellingService} from '../../../../shared/service/selling/selling.service';
import {MatDialog} from '@angular/material/dialog';
import {ProductDetailOrderComponent} from './product-detail-order/product-detail-order.component';
import {Constant} from '../../../../shared/constants/Constant';
import {btoa} from 'buffer';
import {CustomerService} from '../../../../shared/service/customer/customer.service';
import {BehaviorSubject, finalize, Observable} from 'rxjs';
import {map, startWith} from 'rxjs/operators';
// import {CustomerFormComponent} from '../../customer-manager/customer-form/customer-form.component';
import {CurrencyPipe} from '@angular/common';
import {ToastrService} from 'ngx-toastr';
import {MatDrawer} from '@angular/material/sidenav';
// import printJS = require("print-js");
// @ts-ignore
import printJS from 'print-js';
import {ProductService} from '../../../../shared/service/product/product.service';
import {StorageService} from '../../../../shared/service/storage.service';
import {DomSanitizer} from '@angular/platform-browser';
import {GhnService} from '../../../../shared/service/ghn/ghn.service';
import {Ghn} from '../../../../shared/constants/Ghn';
import {ComfirmSellingComponent} from './comfirm-selling/comfirm-selling.component';
import {Regex} from '../../../../shared/validators/Regex';
import {AuthService} from '../../../../shared/service/auth/auth.service';
import { CategoryService } from '../../../../shared/service/category/category.service';

@Component({
    selector: 'selling',
    templateUrl: './selling.component.html',
    styleUrls: ['./selling.component.scss'],
    encapsulation: ViewEncapsulation.None,
})
export class SellingComponent implements OnInit, OnDestroy {
    @ViewChild('drawer') drawer: MatDrawer;
    @ViewChild('name') name: NgModel;
    @ViewChild('address') address: NgModel;
    @ViewChild('phone') phone: NgModel;


    constructor(private sellingService: SellingService,
                private dialog: MatDialog,
                private categoryService: CategoryService,
                private currencyPipe: CurrencyPipe,
                private toast: ToastrService,
                private productService: ProductService,
                private storageService: StorageService,
                private ghnService: GhnService,
                private authService: AuthService) {
    }

    options = {prefix: '', thousands: ',', precision: '0', allowNegative: 'false'}
    pattern = /^[0-9]*$/
    discount = 0;
    customerInput = new FormControl('');
    productInput = new FormControl('');
    quantityDetail: any = [];
    isLoading: boolean;
    listCate;
    listProduct: any;
    tabFocus: any;
    order: any = {};
    orderIndex: any;
    listOrders: any = [];
    listCustomers: any;
    noteDetail: any = [];
    listProducts: any;
    filteredProduct: Observable<any>;
    filteredCustomer: Observable<any>;
    customerName = '';
    customerPhone = '';
    listProductSearch: any = [];
    openModal: boolean = false;
    listTien: any = [];
    customerPayment;
    scanner2: any;
    formality: number = 0;
    provinces!: any[];
    wards!: any[];
    district!: any[];
    proviceName: any;
    wardName: any;
    districtName: any;
    wardId: number = -1;
    provinceId: number = -1;
    districtId: number = -1;
    serviceId: any;
    shippingTotal: number = 0;
    ship_name = '';
    ship_phone = '';
    ship_address = '';
    check_validate: boolean = false;
    openOrder: boolean = false;
    date: any = '';
    employeeName: any;


    ngOnInit(): void {
        this.getListCate();
        this.getListCustomer();
        this.getAllProduct();
        setTimeout(() => this.load(), 2000);
        this.getProvince();
        this.employeeName = this.storageService.getFullNameFromToken();
        console.log(this.employeeName);
        
    }


    over = 'over';
    showFiller = false;
    tabs = [];
    selected = new FormControl(0);

    addTab(selectAfterAdding: boolean) {
        if (this.tabs.length < 15) {
            this.tabs.push(this.tabs[this.tabs.length - 1] + 1);
            this.selected.setValue(this.tabs.length - 1);
            this.customerName = '';
        }
    }

    removeTab(index: number) {
        if (this.tabs.length > 1) {
            this.tabs.splice(index, 1);
            this.listOrders.splice(index, 1);
            this.setOrderLocalStorage(this.listOrders);
            if (this.selected.value == this.tabs.length) {
                this.selected.setValue(this.tabs.length - 1);
            }
            this.getItemByTabs();
        } else {
            this.tabs = [];
            localStorage.removeItem('order');
            this.getItemLocalStorage();
            this.getItemByTabs();
        }
        this.quantityDetail = [];
    }
//customer--------------------------------
    getListCustomer() {
        this.sellingService.getAllUser(1).subscribe(
            {
                next: resp => {
                    this.listCustomers = resp;
                    this.customerFilter();
                    this.tabs = [];
                    this.getItemLocalStorage();
                },
                error: error => {
                    console.log(error);
                    
                }
            }
        )
    }

    getAllProduct() {
        this.sellingService.getAllProduct(1).subscribe({
            next: resp => {
                this.listProductSearch = resp;
                console.log(this.listProductSearch);
                
                this.productFilter();
            },
            error: error => {

            }
        })
    }

    getListCate() {
        this.isLoading = true;
        // this.sellingService.getAllCategories().subscribe(
        //     resp => {
        //         this.isLoading = false;
        //         this.listCate = resp;
        //         this.fillProduct(0);
        //     }, error => {
        //         this.isLoading = false;
        //         console.log(error);
        //     }
        // )
        this.sellingService.getAllCategories(1).subscribe(
            resp => {
                this.listCate = resp;
                console.log(this.listCate);
                this.isLoading = false;
                this.fillProduct(0);
            }, error => {
                this.isLoading = false;
                console.log(error);
            }
        )
    }

    fillProduct(index: any) {
        this.isLoading = true;
        this.listProduct = [];
        if (this.listCate[index].product === undefined) {
            this.sellingService.findProByCate(this.listCate[index].id).subscribe({
                next: resp => {
                    this.isLoading = false;
                    this.listCate[index].product = resp;
                    this.listProduct = this.listCate[index].product;
                    console.log(this.listProduct);
                    
                },
                error: error => {
                    this.isLoading = false;
                    console.log(error)
                }
            }
            )
            // this.sellingService.getProByCate(this.listCate[index].id).subscribe(
            //     resp => {
            //         this.isLoading = false;
            //         this.listCate[index].product = resp;
            //         this.listProduct = this.listCate[index].product;
            //     },
            //     error => {
            //         this.isLoading = false;
            //         console.log(error)
            //     }
            // )
        } else {
            this.isLoading = false;
            this.listProduct = this.listCate[index].product;
        }
    }


    openDialog(product: any) {
        this.productInput.setValue('');
        this.dialog.open(ProductDetailOrderComponent, {
            width: '30vw',
            disableClose: true,
            hasBackdrop: true,
            data: {
                product: product
            }
        }).afterClosed().subscribe(value => {
            if (!(value == null || value == undefined)) {
                console.log(value);
                let hd: any = {};
                hd.id = this.tabs[this.selected.value];
                hd.note = '';
                hd.customer = '';
                hd.detail = {
                    id: value.id,
                    price: value.price,
                    quantity: value.quantityOrder,
                    quantityInventory: value.quantityInventory,
                    color: value.color,
                    size: value.size,
                    name: value.productName,
                    weight: value.weight
                };
                this.pushDataToLocalStorage(hd);
                // localStorage.setItem('order',JSON.stringify(hd));
                //
                // this.getItemLocalStorage();

            }
        })
    }


    getItemLocalStorage() {
        let orders = JSON.parse(localStorage.getItem('order'));
        if (orders === null) {
            this.tabs.push(1);
        } else {
            orders.forEach(order => {
                this.tabs.push((order.id))
            });
        }
        this.listOrders = orders;
    }

    getItemByTabs() {
        this.discount = 0;
        if (this.listOrders === null) {
            let orders: any = [];
            let order: any = {};
            order.id = this.tabs[this.tabs.length - 1];
            order.totalPrice = 0
            order.totalQuantity = 0
            order.note = '';
            order.customer = '';
            order.orderDetail = [];
            order.totalWeight = 0;
            orders.push(order);
            this.order = order;
            this.setOrderLocalStorage(orders);
            this.listOrders = orders;
            return;
        }
        this.orderIndex = this.listOrders.findIndex(o => o.id == this.tabs[this.selected.value]);
        let order = this.listOrders[this.orderIndex];
        if (order === null || order === undefined) {
            order = {};
            order.id = this.tabs[this.tabs.length - 1];
            order.totalPrice = 0;
            order.totalQuantity = 0;
            order.totalWeight = 0;
            order.note = '';
            order.customer = '';
            order.orderDetail = [];
            this.listOrders.push(order);
            this.setOrderLocalStorage(this.listOrders);
            this.orderIndex = this.listOrders.length - 1;
        } else {
            if (order.customer != '') {
                let customer = this.listCustomers.find(cus => cus.id === order.customer);
                this.customerName = customer.fullname;
            } else {
                this.customerName = '';
            }
        }
        this.order = order
        this.quantityDetail = [];
        this.order.orderDetail.forEach(orderDetail => this.quantityDetail.push(orderDetail.quantity));
    }

    pushDataToLocalStorage(item: any) {
        console.log('item-----------');
        console.log(item);
        let orderLocalArray = this.listOrders;
        console.log('listOrders----------');
        console.log(this.listOrders);
        console.log('orderLocalArray---------------');
        console.log(orderLocalArray);
        let orderIndex = orderLocalArray.findIndex(o => o.id == this.tabs[this.selected.value]);
        console.log('orderIndex----------------');
        console.log(orderIndex);
        let order = orderLocalArray[orderIndex];
        if (order === null || order === undefined) {
            let order: any = {};
            order.id = this.tabs[this.selected.value];
            order.totalPrice = item.detail.quantity * item.detail.price;
            order.totalQuantity = item.detail.quantity;
            order.totalWeight = item.detail.quantity * item.detail.weight;
            this.quantityDetail.push(item.detail.quantity);
            order.note = '';
            order.customer = ''
            order.orderDetail = [];
            order.orderDetail.push(item.detail);
            this.order = order;
            orderLocalArray.push(order);
        } else {
            let orderDetailIndex = order.orderDetail.findIndex(o => o.id === item.detail.id);
            let orderDetail = order.orderDetail[orderDetailIndex];
            if (orderDetail === null || orderDetail === undefined) {
                order.orderDetail.push(item.detail);
                this.quantityDetail.push(item.detail.quantity);
                order.totalPrice += item.detail.quantity * item.detail.price;
                order.totalWeight += item.detail.quantity * item.detail.weight;
                order.totalQuantity += item.detail.quantity;
            } else {
                orderDetail.quantity += item.detail.quantity; //40
                orderDetail.quantityInventory = item.detail.quantityInventory;
                if (orderDetail.quantity > item.detail.quantityInventory) {
                    order.totalQuantity += (item.detail.quantity - (orderDetail.quantity - item.detail.quantityInventory));
                    order.totalPrice += (item.detail.quantity - (orderDetail.quantity - item.detail.quantityInventory)) * item.detail.price;
                    order.totalWeight += (item.detail.quantity - (orderDetail.quantity - item.detail.quantityInventory)) * item.detail.weight;
                    orderDetail.quantity = item.detail.quantityInventory;
                } else {
                    order.totalQuantity += item.detail.quantity;
                    order.totalPrice += item.detail.quantity * item.detail.price;
                    order.totalWeight += item.detail.quantity * item.detail.weight;
                }
                this.quantityDetail[orderDetailIndex] = orderDetail.quantity;

            }
            // order.orderDetail[orderDetailIndex] = orderDetail;
            // orderLocalArray[orderIndex] = order;
            this.order = order;
            // }
            this.setOrderLocalStorage(orderLocalArray);
            this.listOrders = orderLocalArray;
        }
    }

    focusOutItem(index) {
        let element = document.getElementById(`note_${index}`);
        if (this.noteDetail[index] == null || this.noteDetail[index] == '') {
            element.style.display = 'none';
        } else {
            element.style.display = 'block';
        }
    }

    old_index = 1;

    setOrderLocalStorage(item: any) {
        localStorage.setItem('order', JSON.stringify(item));
    }

    minusQuantity(index) {
        let orderDetail = this.order.orderDetail[index];
        if (orderDetail.quantity > 0) {
            orderDetail.quantity -= 1;
            this.order.orderDetail[index] = orderDetail;
            this.order.totalQuantity -= 1;
            this.order.totalPrice -= orderDetail.price;
            this.order.totalWeight -= orderDetail.weight;
        }
        this.quantityDetail[index] = this.order.orderDetail[index].quantity;
        if (orderDetail.quantity == 0) {
            this.deleteDetail(index);
        }
        this.setOrderLocalStorage(this.listOrders);
    }

    plusQuantity(index) {
        let orderDetail = this.order.orderDetail[index];
        if (orderDetail.quantity < orderDetail.quantityInventory) {
            orderDetail.quantity += 1;
            this.order.orderDetail[index] = orderDetail;
            this.order.totalQuantity += 1;
            this.order.totalPrice += orderDetail.price;
            this.order.totalWeight += orderDetail.weight;
        }
        this.quantityDetail[index] = this.order.orderDetail[index].quantity;
        this.setOrderLocalStorage(this.listOrders);
    }

    checkQuantityInput(index) {
        // let pattern = /^[0-9]*$/
        // if (this.quantityDetail[index] == '') {
        //     return;
        // }
        // if (!isNaN(this.quantityDetail[index])) {
        this.order.orderDetail[index].quantity = parseInt(this.quantityDetail[index]);
        if (this.order.orderDetail[index].quantity > this.order.orderDetail[index].quantityInventory) {
            this.order.orderDetail[index].quantity = this.order.orderDetail[index].quantityInventory;
        }
        // } else {
        //     this.quantityDetail[index] = 1;
        // }
        this.quantityDetail[index] = this.order.orderDetail[index].quantity;
        var totalPrice: number = 0;
        var totalQuantity: number = 0;
        var totalWeight: number = 0;
        this.order.orderDetail.forEach(orderDt => {
            totalPrice += orderDt.price * orderDt.quantity;
            totalQuantity += orderDt.quantity;
            totalWeight += orderDt.weight * orderDt.quantity;
        });
        this.order.totalQuantity = totalQuantity;
        this.order.totalPrice = totalPrice;
        this.order.totalWeight = totalWeight;
    }

    debounce(fn, ms) {
        let timer;
        return function () {
            // Nhận các đối số
            const args = arguments;
            const context = this;

            if (timer) {
                clearTimeout(timer);
            }

            timer = setTimeout(() => {
                fn.apply(context, args);
            }, ms)
        }
    }

    load() {
        document.getElementById('noteOrder').addEventListener('keyup', this.debounce((event) => {
            this.setOrderLocalStorage(this.listOrders);
        }, 2000));

    }


    blurQuantityInput(index) {
        if (this.quantityDetail[index] == '') {
            this.quantityDetail[index] = this.order.orderDetail[index].quantity;
        }
    }

    deleteDetail(index) {
        let oderDetailDelete = this.order.orderDetail.splice(index, 1);
        this.quantityDetail.splice(index, 1);
        this.order.totalPrice -= (oderDetailDelete[0].price * oderDetailDelete[0].quantity);
        this.order.totalQuantity -= oderDetailDelete[0].quantity;
        this.order.totalWeight -= (oderDetailDelete[0].weight * oderDetailDelete[0].quantity);
        this.setOrderLocalStorage(this.listOrders);
    }

    ngOnDestroy() {
        this.setOrderLocalStorage(this.listOrders);
    }

    customerFilter() {
        this.filteredCustomer = this.customerInput.valueChanges.pipe(
            startWith(''),
            map(value => this._filter(value || '')),
        );
    }

    _filter(value: any): any[] {
        var filterValue;
        if (isNaN(value)) {
            filterValue = value.toLowerCase();
        } else {
            filterValue = value;
        }
        return this.listCustomers.filter(option => option.firstName.toLowerCase().includes(filterValue)
            || option.email.toLowerCase().includes(filterValue)
            || option.phoneNumber.toLowerCase().includes(filterValue)
            || option.lastName.toLowerCase().includes(filterValue));
    }

    productFilter() {
        this.filteredProduct = this.productInput.valueChanges.pipe(
            startWith(''),
            map(value => this._filterproduct(value || '')),
        );
    }

    _filterproduct(value: any): any[] {
        var filterValue;
        if (isNaN(value)) {
            filterValue = value.toLowerCase();
        } else {
            filterValue = value;
        }
        return this.listProductSearch.filter(option => option.productName?.toLowerCase().includes(filterValue)
            || option.productName?.includes(filterValue));
    }

    deleteCustomer() {
        this.order.customer = '';
        this.customerInput.setValue('');
        this.customerName = '';
        this.ship_name = '';
        this.setOrderLocalStorage(this.listOrders);
    }

    chooseCustomer() {
        this.customerName = '';
        let customer = this.listCustomers.find(cus => cus.id == this.customerInput.value);
        console.log(customer);
        this.order.customer = customer.id
        this.customerName = customer.lastName + ' ' + customer.firstName;
        this.customerPhone = customer.phoneNumber;
        this.customerInput.setValue('');
        this.ship_name = this.customerName;
        this.ship_phone = this.customerPhone;
        console.log(this.ship_phone);
        
        
        this.setOrderLocalStorage(this.listOrders);
    }

    openDialogAddKh() {
        // this.dialog.open(CustomerFormComponent, {
        //     width: '1000px',
        //     disableClose: true,
        //     hasBackdrop: true,
        //     data: {
        //         type: Constant.TYPE_DIALOG.NEW
        //     }
        // }).afterClosed().subscribe(result => {
        //     // this.customerService.response.subscribe(data=>console.log(data))
        //     this.customerService.response.subscribe(
        //         resp => {
        //             if (resp != null) {
        //                 this.order.customer = resp?.id;
        //                 this.customerName = resp?.fullname;
        //                 this.customerService.response.next(null);
        //             }
        //         }
        //     )

        //     this.getListCustomer();
        // })
    }

    openModalfocus() {
        this.openModal = true;
    }

    closeModal() {
        this.openModal = false;
    }

    tinhtien() {
        console.log(this.ship_phone);
        
        // this.discount = 0;
        this.date = this.formatDate(new Date());
        this.listTien = [];
        var total = this.order.totalPrice - this.order.totalPrice * this.discount / 100;
        this.customerPayment = total;
        if (this.discount == 100) {
            this.customerPayment = 0;
        }
        if (total % 1000 != 0) {
            total = Math.ceil(total / 1000) * 1000;
            this.listTien.push(total);
        }
        if (total % 5000 != 0) {
            total = Math.ceil(total / 5000) * 5000;
            this.listTien.push(total);
        }
        if (total % 10000 != 0) {
            total = Math.ceil(total / 10000) * 10000;
            this.listTien.push(total);
        }
        if (total % 20000 != 0) {
            total = Math.ceil(total / 20000) * 20000;
            this.listTien.push(total);
        }
        if (total % 50000 != 0) {
            total = Math.ceil(total / 50000) * 50000;
            this.listTien.push(total);
        }
        if (total % 100000 != 0) {
            total = Math.ceil(total / 100000) * 100000;
            this.listTien.push(total);
            if (total % 500000 == 0) {
                return;
            }
        }
        if (total % 200000 != 0) {
            total = Math.ceil(total / 200000) * 200000;
            this.listTien.push(total);
        }
        if (total % 500000 != 0) {
            total = Math.ceil(total / 500000) * 500000;
            this.listTien.push(total);
        }

        console.log(this.listTien);
    }

    clickPrice(index) {
        this.customerPayment = this.listTien[index];
    }

    resetQuantityInventory() {
        let lstProductId = [];
        this.order.orderDetail.forEach(pro => lstProductId.push(pro.id));
        this.sellingService.resetQuantityInventory(lstProductId).subscribe({
            next: resp => {
                let lst = resp;
                lst.forEach(pro => {
                    let index = this.order.orderDetail.findIndex(orderDetal => orderDetal.id == pro.id);
                    this.order.orderDetail[index].quantityInventory = pro.quantity;
                });
            },
            error: err => {

            }
        })
    }


    selling(status: number) {
        console.log(this.order);
        if (this.order.orderDetail.length == 0) {
            this.toast.error('Chưa có sản phẩm nào!');
            return;
        }
        if (status == 1) {
            this.check_validate = false;
            if (this.wardId == -1) {
                this.toast.error('Vui lòng chọn địa chỉ nhận hàng!');
                return;
            }
            this.checkValidate();
            if (this.check_validate) {
                this.toast.error('Vui lòng kiểm tra lại các trường!');
                return;
            }
            const rexgex = /(0?)(3[2-9]|5[6|8|9]|7[0|6-9]|8[0-6|8|9]|9[0-4|6-9])[0-9]{7}$/;
            if (!rexgex.test(this.ship_phone)) {
                this.toast.error('Số điện thoại không hợp lệ!');
                return;
            }
        }
        for (let i = 0; i < this.quantityDetail.length; i++) {
            if (this.quantityDetail[i] > this.order.orderDetail[i].quantityInventory || this.quantityDetail[i] == '') {
                this.toast.error('Vui lòng kiểm tra lại số lượng của sản phẩm!');
                return;
            }
        }
        let shipAddress = this.ship_address + ', ' + this.wardName + ', ' + this.districtName + ', thành phố ' + this.proviceName;
        this.order.shipAddress = shipAddress;
        this.order.shipPhone = this.ship_phone;
        this.order.shipName = this.ship_name;
        this.order.freight = this.shippingTotal;
        this.order.discount = status == 0 ? this.discount : 0;
        this.order.checkSelling = status;
        this.order.employee = this.storageService.getIdFromToken();
        this.dialog.open(ComfirmSellingComponent, {
            width: '35vw',
            disableClose: true,
            hasBackdrop: true,
            data: {
                order: status == 1 ? this.order : null
            }
        }).afterClosed().subscribe(value => {
            if (value == Constant.RESULT_CLOSE_DIALOG.CONFIRM) {
                this.toast.warning('Đặt hàng sắp thành công')
                // this.isLoading = true;
                // this.sellingService.paymentSelling(this.order).subscribe({
                //     next: resp => {
                //         this.drawer.close();
                //         this.isLoading = false;
                //         if (status == 0) {
                //             this.toast.success('Thành công');
                //             this.print(resp.data);
                //         } else {
                //             this.toast.success('Đặt hàng thành công');
                //         }
                //         this.removeTab(this.selected.value);
                //     },
                //     error: err => {
                //         if (err.error?.code == 'LIMIT_QUANTITY') {
                //             this.toast.error(err.error.message);
                //             this.resetQuantityInventory();
                //         } else {
                //             this.toast.error('Lỗi thanh toán!');
                //         }
                //     }
                // })
            }
        })
    }

    openDialogSacnner(template: TemplateRef<any>) {
        this.scanner2 = this.dialog.open(template, {
            width: '500px',
            disableClose: true,
            hasBackdrop: true,
        });
    }


    print(data) {
        let customer = this.listCustomers.find(cus => cus.id == data.customer)
        const formatter = new Intl.NumberFormat();
        let text = '';
        data.orderDetail.forEach(od => {
            text += `<tr>
                            <td ><div>${od.name} (${od.nameSize}/${od.nameColor})</div>
                                <div>${od.quantity}</div></td>
                            <td style="text-align: center">${formatter.format(od.price)}</td>
                            <td style="text-align: end">${formatter.format(od.quantity * od.price)}</td>
                    </tr>`
        })
        // @ts-ignore
        printJS({
            printable: 'demo',
            properties: [{
                field: ' ',
                displayName: ' '
            }],
            documentTitle: 'NemPhaSun',

            type: 'html',
            maxWidth: 826,
            font: 'Thoma',
            font_size: '12pt',
            header: `
                    <div class="custom"> 
                    <h2>NemPhaSun</h2> <h3 >HÓA ĐƠN BÁN HÀNG </h3>
                    </div>
                    <div class="info">
                        <div>Mã hóa đơn: ${data.id}</div>
                        <div>Khách hàng: ${customer == undefined ? 'Khách lẻ' : customer.fullname}</div>
                        <div>SĐT: ${customer == undefined ? '--' : customer.phone}</div>
                    </div>
                    <div >
                        <table class="table">
                        <thead>
                            <tr>
                                <th style="text-align: start; width: 50%">Số lượng</th>
                                <th style="width:25%">Đơn giá</th>
                                <th style="text-align: end; width:25%">Thành tiền</th>
                            </tr>
                        </thead>
                        <tbody>
                            ${text}     
                        </tbody>
                        </table>
                    </div style="">
                        <table class="total" style="">
                            <thead>
                                <th></th>
                                <th></th>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>Tổng tiền hàng:</td>
                                    <td>${formatter.format(data.totalPrice)}</td>
                                </tr>
                                <tr>
                                    <td>Chiếu khấu ${data.discount}%:</td>
                                    <td>${formatter.format(data.totalPrice * data.discount / 100)}</td>
                                </tr>
                                <tr>
                                    <td>Tổng thanh toán:</td>
                                    <td>${formatter.format(data.totalPrice - (data.totalPrice * data.discount / 100))}</td>
                                </tr>
                            </tbody>     
                        </table>                    
                    <div style="margin-top: 20px;text-align: center;font-size: 13px">
                        <div>Quý khách vui lòng kiểm tra kỹ hàng</div>
                        <div>--------------------</div>
                        <div>Cảm ơn quý khách, hẹn gặp lại!</div>  
                    </div>
`,
            style: '.custom { text-align: center;font-size:13px;margin:0 }' +
                '.info {margin-top:5px; margin-bot: 10px; font-size:13px}' +
                '.table{width: 100%; font-size: 13px; margin-top: 10px; ' +
                'border-top:2px dashed  black; border-bottom: 2px dashed  black}' +
                '.total{width: 100%; float: right; text-align: end; font-weight: bold;' +
                'font-size: 13px;margin:10px 0px 15px 0px; border-bottom: 2px dashed black;padding-bottom:10px}'
        })
        // printJS(data, 'html')
    }


    availableDevices: MediaDeviceInfo[];
    currentDevice: MediaDeviceInfo = null;
    hasDevices: boolean;
    hasPermission: boolean;

    qrResultString: string;

    torchEnabled = false;
    torchAvailable$ = new BehaviorSubject<boolean>(false);
    tryHarder = false;


    clearResult(): void {
        this.qrResultString = null;
    }

    onCamerasFound(devices: MediaDeviceInfo[]): void {
        this.availableDevices = devices;
        this.hasDevices = Boolean(devices && devices.length);
    }

    onCodeResult(resultString: string) {
        this.qrResultString = '0';
        let qrResult = resultString.substring(0, resultString.length - 1);
        this.productService.getByBarcode(qrResult).subscribe({
                next: resp => {
                    if (resp != null) {
                        if (resp.quantity > 0 && resp.color.status == 1) {
                            this.qrResultString = '1';
                            let hd: any = {};
                            hd.id = this.tabs[this.selected.value];
                            hd.note = '';
                            hd.customer = '';
                            hd.detail = {
                                id: resp.id,
                                price: resp.product.price,
                                quantity: 1,
                                quantityInventory: resp.quantity,
                                colorId: resp.color.id,
                                colorCode: resp.color.code,
                                sizeId: resp.size.id,
                                sizeCode: resp.size.code,
                                name: resp.product.name,
                            };

                            this.pushDataToLocalStorage(hd);
                            this.toast.success('Thêm sản phẩm thành công');
                            document.getElementById('qrResult').innerHTML = `${resp.product.name}(
                                                    <span style="width: 15px;height: 15px;background-color: ${resp.color.code}; display: inline-block">
                                                    </span>/${resp.size.code})`;
                        } else {
                            this.toast.error('Số lượng không đủ hoặc đã ngừng bán');
                            document.getElementById('qrResult').innerHTML = '';
                        }
                    } else {
                        this.toast.error('Không tìm thấy sản phẩm');
                        document.getElementById('qrResult').innerHTML = '';
                    }
                },
                error: err => {
                    console.log(err);
                    this.toast.error('Lỗi quét Barcode');
                    document.getElementById('qrResult').innerHTML = '';
                }
            }
        )
    }

    onDeviceSelectChange(selected: string) {
        const device = this.availableDevices.find(x => x.deviceId === selected);
        this.currentDevice = device || null;
    }

    onHasPermission(has: boolean) {
        this.hasPermission = has;
    }

    onTorchCompatible(isCompatible: boolean): void {
        this.torchAvailable$.next(isCompatible || false);
    }

    toggleTorch(): void {
        this.torchEnabled = !this.torchEnabled;
    }

    toggleTryHarder(): void {
        this.tryHarder = !this.tryHarder;
    }


    SHIP

    getProvince() {
        this.ghnService.getProvince().subscribe((res: any) => {
            this.provinces = res.data;
        })
    }

    //
    getDistrict(provinceId: any, provinceName: any) {
        let data = {'province_id': provinceId};
        this.ghnService.getDistrict(data).subscribe((res: any) => {
            this.district = res.data;
        })
        this.proviceName = provinceName;
    }

    //
    getWard(districtId: any, districtName: any) {
        let data = {'district_id': districtId};
        this.ghnService.getWard(data).subscribe((res: any) => {
            this.wards = res.data;
        })
        this.districtName = districtName;
        this.districtId = districtId;
    }

    resetDistrictAndWard() {
        this.wardId = -1;
        this.districtId = -1;
        this.district = [];
        this.wards = [];
        this.shippingTotal = 0;
    }

    resetWard() {
        this.shippingTotal = 0;
        this.wardId = -1;
        this.wards = [];
    }

    getWardName(wardName: any) {
        this.wardName = wardName;
        this.getShippingFee(this.districtId);
    }

    //Api tinh phí vận chuyển
    getShippingFee(districtId: any) {
        this.isLoading = true;
        const data = {
            'shop_id': Ghn.SHOP_ID_NUMBER,
            'from_district': 3440,
            'to_district': districtId
        }
        //Get service để lấy ra phương thức vận chuyển: đường bay, đường bộ,..
        this.ghnService.getService(data).subscribe((res: any) => {
            console.log(res.data)
            this.serviceId = res.data[0].service_id;
            const shippingOrder = {
                'service_id': this.serviceId,
                'insurance_value': this.order.totalPrice,
                'from_district_id': 3440,
                'to_district_id': data.to_district,
                'weight': this.order.totalWeight
            }
            //getShippingOrder tính phí vận chuyển
            this.ghnService.getShippingOrder(shippingOrder)
                .pipe(finalize(() => {
                    this.isLoading = false;
                }))
                .subscribe((res: any) => {
                    this.shippingTotal = res.data.total;
                })
        })
    }

    checkValidate() {
        if (this.ship_name.trim() == '') {
            document.getElementById('customer-name-order').style.border = '1px solid red';
            this.check_validate = true;
        }
        if (this.ship_phone.trim() == '') {
            document.getElementById('customer-phone-order').style.border = '1px solid red';
            this.check_validate = true;
        }
        if (this.ship_address.trim() == '') {
            document.getElementById('customer-other-address').style.border = '1px solid red';
            this.check_validate = true;
        }
    }

    clearDataOrder() {
        this.resetDistrictAndWard();
        if (this.customerName.length > 0) {
            this.ship_name = this.customerName;
        } else {
            this.name.reset('');
        }
        if (this.ship_phone.length > 0) {
            this.ship_phone = this.customerPhone;
        }else{
            this.phone.reset('');
        }
        this.address.reset('');
        document.getElementById('customer-name-order').style.border = 'none';
        document.getElementById('customer-other-address').style.border = 'none';
        document.getElementById('customer-phone-order').style.border = 'none';
    }


    // FORMAT DATE
    formatDate(date) {
        return (
            [
                this.padTo2Digits(date.getDate()),
                this.padTo2Digits(date.getMonth() + 1),
                date.getFullYear(),
            ].join('/') +
            ' ' +
            [
                this.padTo2Digits(date.getHours()),
                this.padTo2Digits(date.getMinutes()),
            ].join(':')
        );
    }


    padTo2Digits(num) {
        return num.toString().padStart(2, '0');
    }

    onLogout() {
        this.authService.logout();
    }
}


