export default {
    'test': 'Ví dụ',
    dashboard: {
        newOrderCount: 'Tổng đơn hàng mới',
        totalRevenue: 'Tổng doanh thu',
        paymentPending: 'Chờ thanh toán',
        orderCompleted: 'Tổng đơn hàng hoàn tất tháng này',
        orderCancelledCount: 'Đã hủy',
        recentRevenueIn7Days: 'Doanh thu trong 7 ngày gần nhất',

        orderLatest: {
            title: 'Đơn hàng mới nhất',
            table: {
                orderNo: 'Mã đơn hàng',
                createdBy: 'Khách hàng',
                subscriptionPack: 'Gói đăng ký',
                price: 'Giá',
                createdAt: 'Ngày tạo',
                action: 'Thao tác',
            }
        }
    },
    statistic: {
        revenue: {
            title: 'Thống kê doanh thu',
            totalRevenueThisMonth: 'Tổng doanh thu tháng này',
            totalSubscriber: 'Tổng số người đăng ký tháng này',
            totalSubscriberOnTrial: 'Tổng số người đăng ký dùng thử tháng này',
            totalRevenueBySubsPack: 'Doanh thu theo gói đăng ký',
            revenueByDay: 'Doanh thu theo ngày',
            monthlyRevenue: 'Doanh thu hàng tháng',
        },
        registrationStatus: {
            title: 'Trạng thái đăng ký',
            table: {
                'user': 'Khách hàng',
                'subscriptionPack': 'Gói đăng ký',
                'expiredDate': 'Ngày hết hạn',
                action: {
                    sendEmail: 'Gửi email',
                }
            }
        },
        registrationRate: {
            title: 'Tỉ lệ người đăng ký hàng tháng',
        },
    },
    manga: {
        form:
        {
            'sure-delete': 'Bạn chăc chắn muốn xóa manga này?',
            errors: {
                'check-again': 'Vui lòng kiểm tra lại thông tin!',
                'title-required': 'Tiêu đề không được để trống!',
                'title-max': 'Tiêu đề không được quá 255 ký tự!',
                'content-required': 'Vui lòng nhập nội dung',
                'genres-required': 'Vui lòng chọn thể loại',
                'authors-required': 'Vui lòng chọn tác giả',
                'tags-required': 'Vui lòng chọn thẻ',
                'featured-image-required': 'Vui lòng chọn ảnh',
                'featured-image-invalid': 'Định dạng không hợp lệ',
                'volume-required': 'Vui lòng thêm tập!',
                'chapter-name-required': 'Tiêu đề không được bỏ trống!',
                'chapter-images-required': 'Ảnh không được để trống!',
                'chapter-content-required': 'Nội dung không được để trống!',
                'create-failed': 'Thêm thất bại!',
                'create-success': 'Thêm thành công!',
                'edit-failed': 'Sửa thất bại!',
                'edit-success': 'Sửa thành công!',
                'delete-success': 'Xóa thành công!',
                'delete-failed': 'Xóa thất bại!',
            },
            volume: {
                'volume': 'Tập',
                'volume-empty': 'Hiện chưa có tập nào!',
                'add-volume': 'Thêm tập mới',
                'edit-volume': 'Sửa tập',
                'volume-name': 'Tên tập',
                'volume-name-placeholder': 'Nhập tên tập',
                'save-volume-btn': 'Lưu',
                'add-volume-btn': 'Thêm tập',
                'edit-volume-btn': 'Sửa tập',
                'cancel-volume-btn': 'Hủy',
                'sure-delete': 'Bạn chăc chắn muốn xóa tập này?',
                'delete-success': 'Xóa thành công!',
                'delete-failed': 'Xóa thất bại!',
                errors: {
                    'check-again': 'Vui lòng kiểm tra lại thông tin!',
                    'volume-name-required': 'Tên tập không được để trống!',
                    'volume-name-max': 'Tên tập không được quá 255 ký tự!',
                    'add-failed': 'Thêm thất bại!',
                    'add-success': 'Thêm thành công!',
                    'edit-failed': 'Sửa thất bại!',
                    'edit-success': 'Sửa thành công!'
                }
            },
            chapter: {
                'chapter': 'Chương',
                'add-chapter': 'Thêm chương mới',
                'sure-delete': 'Bạn chăc chắn muốn xóa chương này?',
                'save-chapter-btn': 'Lưu',
                'add-chapter-btn': 'Thêm chương',
                'edit-chapter-btn': 'Chỉnh sửa',
                'empty-chapter': 'Hiện chưa có chương nào!',
                'delete-success': 'Xóa thành công!',
                'delete-failed': 'Xóa thất bại!',
                errors: {
                    'file-size-exceed': 'Kích thước file không được vượt quá 5MB!',
                    'total-file-size-exceed-100mb': 'Kích thước tổng số file không được vượt quá 100MB!',
                    'exceed-character': 'Số ký tự không được vượt quá 30000 ký tự!',
                    'only-txt-allowed': 'Chỉ được tải file txt!',
                }

            }
        },
        table: {
            title: 'Tiêu đề',
            mangaType: 'Loại truyện',
            releaseStatus: 'Trạng thái phát hành',
            genre: 'Thể loại',
            author: 'Tác giả',
            tag: 'Thẻ',
            action: 'Hành động',
            status: 'Trạng thái',
            createdBy: 'Người tạo',
            modifiedBy: 'Người cập nhật',
            createdAt: 'Ngày tạo',
            modifiedAt: 'Ngày cập nhật'
        },
        eReleaseStatus: {
            'COMING': 'Sắp ra mắt',
            'GOING': 'Đang ra',
            'COMPLETED': 'Đã hoàn thành',
            'ON_STOPPING': 'Tạm ngừng',
            'STOPPED': 'Đã ngừng',
        },
        eMangaType: {
            'IMAGE': 'Truyện tranh',
            'TEXT': 'Truyện chữ',
        },
        eMangaStatus: {
            'ALL': 'Tất cả',
            'PUBLISHED': 'Đang hiển thị',
            'HIDDEN': 'Đang ẩn',
        }
    },
    comment: {
        'page-title': 'Danh sách bình luận',
        'comment-type-title': 'Loại bình luận:',
        table: {
            'user': 'Người bình luận',
            'content': 'Nội dung',
            'createdAt': 'Ngày tạo',
            'action': 'Hành động'
        }
    },
    user: {
        'page-title': 'Danh sách người dùng',
        'add-btn': 'Thêm mới',
        table: {
            'fullName': 'Họ và tên',
            'username': 'Tên đăng nhập',
            'email': 'Email',
            'phone': 'Số điện thoại',
            'sex': 'Giới tính',
            'avatar': 'Ảnh đại diện',
            'status': 'Trạng thái',
            'createdAt': 'Ngày tạo',
            role: 'Vai trò',
            'accountType': 'Loại tài khoản',
            'action': 'Hành động',
            'status-item': {
                NOT_ENABLED: 'Chưa kích hoạt',
                ACTIVED: 'Đang hoạt động',
                DEACTIVED: 'Đã khóa'
            },
            'accountType-item': {
                FACEBOOK: 'Facebook',
                GOOGLE: 'Google',
                DATABASE: 'Tài khoản thường'
            },
            'role-item': {
                ADMIN: 'Quản lý',
                EMP: 'Nhân viên',
                CUS: 'Khách hàng'
                
            },
            'status-item-select': {
                ACTIVED: 'Kích hoạt',
                DEACTIVED: 'Khóa'
            },
            'change-status-success': 'Thay đổi trạng thái thành công!',
            'change-status-failed': 'Thay đổi trạng thái thất bại!',
        },
        modal: {
            'add-title': 'Thêm người dùng mới',
            'edit-title': 'Chỉnh sửa thông tin',
        },
        form: {
            'fullName': 'Họ và tên',
            'username': 'Tên đăng nhập',
            'email': 'Email',
            'phone': 'Số điện thoại',
            'sex': 'Giới tính',
            'password': 'Mật khẩu',
            'confirm-password': 'Nhập lại mật khẩu',
            'authority': 'Quyền',
            'save-btn': 'Lưu',
            'reset-btn': 'Làm mới',
            'edit-btn': 'Chỉnh sửa',
            'delete-btn': 'Xóa',
            'sure-delete': 'Bạn chắc chắn muốn xóa người dùng này?',
            'delete-success': 'Xóa thành công!',
            'delete-failed': 'Xóa thất bại!',
            'sex-radio': {
                male: 'Nam',
                female: 'Nữ',
            },
            'add-success': 'Thêm thành công!',
            'add-failed': 'Thêm thất bại!',
            'edit-success': 'Sửa thành công!',
            'edit-failed': 'Sửa thất bại!',
            errors: {
                'check-again': 'Vui lòng kiểm tra lại thông tin!',
                'fullName-required': 'Họ và tên không được để trống!',
                'fullName-max': 'Họ và tên không được quá 255 ký tự!',
                'username-required': 'Tên đăng nhập không được để trống!',
                'username-max': 'Tên đăng nhập không được quá 255 ký tự!',
                'email-required': 'Email không được để trống!',
                'email-max': 'Email không được quá 255 ký tự!',
                'email-invalid': 'Email không đúng định dạng!',
                'phone-required': 'Số điện thoại không được để trống!',
                'phone-max': 'Số điện thoại không được quá 255 ký tự!',
                'phone-invalid': 'Số điện thoại không đúng định dạng!',
                'password-required': 'Mật khẩu không được để trống!',
                'confirm-password-required': 'Vui lòng nhập lại mật khẩu!',
                'confirm-password-not-match': 'Mật khẩu không khớp!',
                'password-max': 'Mật khẩu không được quá 30 ký tự!',
                'authority-required': 'Vui lòng chọn quyền',
                'role-required': 'Vui lòng chọn vai trò',

            }
        },
        authority: {
            ROLE_ADD_EDIT_USER: 'Thêm/Sửa người dùng',
            ROLE_DELETE_USER: 'Xóa người dùng',

            ROLE_ADD_EDIT_MANGA: 'Thêm/Sửa truyện',
            ROLE_DELETE_MANGA: 'Xóa truyện',

            ROLE_ADD_EDIT_MANGA_GENRE: 'Thêm/Sửa thể loại truyện',
            ROLE_DELETE_MANGA_GENRE: 'Xóa thể loại truyện',

            ROLE_ADD_EDIT_MANGA_AUTHOR: 'Thêm/Sửa tác giả truyện',
            ROLE_DELETE_MANGA_AUTHOR: 'Xóa tác giả truyện',

            ROLE_ADD_EDIT_ORDER: 'Thêm/Sửa đơn hàng',
            ROLE_DELETE_ORDER: 'Xóa đơn hàng',

            ROLE_ADD_EDIT_CATEGORY: 'Thêm/Sửa thể loại bài viết',
            ROLE_DELETE_CATEGORY: 'Xóa thể loại bài viết',

            ROLE_ADD_EDIT_POST: 'Thêm/Sửa bài viết',
            ROLE_DELETE_POST: 'Xóa bài viết',

            ROLE_ADD_EDIT_COMMENT: 'Thêm/Sửa bình luận',
            ROLE_DELETE_COMMENT: 'Xóa bình luận',

            ROLE_ADD_EDIT_TAG: 'Thêm/Sửa tag',
            ROLE_DELETE_TAG: 'Xóa tag',

            ROLE_VIEW_STATISTIC: 'Xem thống kê',

            ROLE_ADMIN: 'Quản trị viên',
            ROLE_USER: 'Người dùng',
        }
    },
    order: {
        'page-title': 'Danh sách đơn hàng',
        addBtn: 'Thêm mới',
        'delete-success': 'Xóa thành công!',
        'delete-failed': 'Xóa thất bại!',
        table: {
            'orderNumber': 'Mã đơn hàng',
            'subscription': 'Gói đăng ký',
            'finalPrice': 'Giá tiền',
            'paymentMethod': 'Phương thức thanh toán',
            'status': 'Trạng thái',
            'phone': 'Số điện thoại',
            'orderType': 'Loại đơn hàng',
            'createdAt': 'Ngày tạo',
            'updatedAt': 'Ngày sửa',
            'createdBy': 'Khách hàng',
            'modifiedBy': 'Người cập nhật',
            'upgrade': 'Nâng cấp',
            renew: 'Gia hạn',
            viewDetail: 'Chi tiết',
            upgradeSubs: 'Nâng cấp gói',
            originalOrderNumber: 'Mã đơn hàng gốc',
            timeRange: 'Thời gian',
            keyword: 'Từ khóa',
        },
        modal: {
            addTitle: 'Thêm mới',
            editTitle: 'Chỉnh sửa thông tin',
            form: {
                labels: {
                    'orderNumber': 'Mã đơn hàng',
                    subscriptionPack: 'Gói đăng ký',
                    choosePack: 'Chọn gói đăng ký',
                    price: 'Giá tiền',
                    paymentMethod: 'Phương thức thanh toán',
                    choosePaymentMethod: 'Chọn phương thức thanh toán',
                    'createdBy': 'Khách hàng',
                    status: 'Trạng thái',
                    chooseUser: 'Chọn người dùng',
                    'sure-complete': 'Bạn chắc chắn muốn hoàn tất đơn hàng này?',
                    'mark-complete': 'Cập nhật hoàn tất',
                }
            },
            actions: {
                'add-success': 'Thêm thành công!',
                'add-failed': 'Thêm thất bại!',
                'edit-success': 'Sửa thành công!',
                'edit-failed': 'Sửa thất bại!',
                'update-status-success': 'Cập nhật trạng thái thành công!',
                'update-status-failed': 'Cập nhật trạng thái thất bại!',
            },
            errors: {
                'required-user': 'Vui lòng chọn người dùng!',
                'required-subs': 'Vui lòng chọn gói đăng ký!',

            }
        },
        upgradeModal: {
            title: 'Nâng cấp gói đọc',
            currentSubscriptionPack: 'Gói hiện tại',
            chooseUpgradeSubscriptionPack: 'Chọn gói nâng cấp',
            needPriceToPay: 'Số tiền cần thanh toán:',
            createOrder: 'Tạo đơn hàng',
            newPack: 'Gói mới',
            oldPack: 'Gói cũ',
            paymentMethod: 'Phương thức thanh toán:',
            createSuccess: 'Tạo thành công!',
            createFailed: 'Tạo thất bại!',
        },
        detailModal: {
            title: 'Chi tiết đơn hàng',
        },
        eStatus: {
            'ALL': 'Tất cả',
            'PENDING_PAYMENT': 'Chờ thanh toán',
            'CANCELED': 'Đã hủy',
            'COMPLETED': 'Hoàn tất',
            'USER_CONFIRMED_BANKING': 'Chờ kiểm tra CK',
        },
        ePaymentMethod: {
            'ALL': 'Tất cả',
            'ATM': 'Chuyển khoản',
            'VN_PAY': 'VN Pay',
        }
    },
    'subscription-pack': {
        'page-title': 'Danh sách gói đọc',
        addBtn: 'Thêm mới',
        table: {
            'subsCode': 'Mã gói',
            'name': 'Tên gói',
            'price': 'Giá gói',
            'limitedMonthCount': 'Số tháng đọc',
            'createdAt': 'Ngày tạo',
            'modifiedAt': 'Ngày sửa',
            'createdBy': 'Người tạo',
            'modifiedBy': 'Người sửa',
            'action': 'Hành động',
            'status': 'Trạng thái hiển thị',
            'sure-delete': 'Bạn chắc chắn muốn xóa gói đọc này?',
            'all': 'Tất cả',
            'deleted': 'Xóa mềm',
            active: 'Đang hiển thị',
            'inactive': 'Đang ẩn',
            edit: 'Chỉnh sửa',
            actions: {
                hide: 'Ẩn',
                show: 'Hiển thị',
            }
        },
        modal: {
            addTitle: "Thêm gói đọc mới",
            editTitle: "Chỉnh sửa gói đọc",
            month: "Tháng",
            name: "Gói",
            price: "Giá",
            resetBtn: "Làm mới",
            saveBtn: "Lưu",
            dateCount: "Thời hạn gói",
            description: "Mô tả",
            form: {
                'add-success': 'Thêm thành công!',
                'add-failed': 'Thêm thất bại!',
                'edit-success': 'Sửa thành công!',
                'edit-failed': 'Sửa thất bại!',
                'delete-success': 'Xóa thành công!',
                'delete-failed': 'Xóa thất bại!',
                'toggle-failed': 'Thay đổi trạng thái thất bại!',
                'toggle-success': 'Thay đổi trạng thái thành công!',
            },
            placeholders: {
                title: "Nhập tên gói",
                price: "Nhập giá",
                "choose-month": "Chọn thời hạn gói",
            },
            errors: {
                "required-price": "Giá gói không được để trống",
                "required-dateCount": "Vui lòng chọn hời hạn gói",
                "min-price": "Giá gói tối thiểu phải là 10.000vnd",
                "max-price": "Giá gói không được quá 100.000.000vnd",
                "exceed-discount-price": 'Giá sau khi giảm không được lớn hơn giá gốc',
                'check-input-again': 'Vui lòng kiểm tra lại thông tin!',
            }

        },
    },
    login: {
        placeholders: {
            username: 'Tên đăng nhập',
            password: 'Mật khẩu',
            remember: 'Nhớ mật khẩu',
            forgot: 'Quên mật khẩu?',
            email: 'Địa chỉ email',
        },
        buttons: {
            login: 'Đăng nhập',
            send: 'Gửi'
        },
        modal: {
            title: 'Quên mật khẩu',
            form: {
                labels: {
                    email: 'Nhập email của bạn để lấy lại mật khẩu:',
                }
            },
            errors: {
                'check-again': 'Vui lòng kiểm tra lại thông tin!',
                email: 'Vui lòng nhập emai!',
            },
            'forgot-success': 'Mật khẩu mới đã được gửi vào email của bạn!',
            'forgot-failed': 'Lỗi server!',
        },
        errors: {
            'login-failed': 'Tên đăng nhập hoặc mật khẩu không đúng!',
            'login-success': 'Đăng nhập thành công!',
        },
        'logout-success': 'Đăng xuất thành công!',
    },
    'confirm-yes': 'Có',
    'confirm-no': 'Không',
    'reset': 'Làm mới',
    placeholders: {
        'search': 'Tìm kiếm...',
    },
    response: {
        errors: {
            'code-0': 'Lỗi server. Vui lòng thử lại sau!',
            'code-11': 'Tên đăng nhập đã tồn tại!',
            'code-12': 'Email đã tồn tại!',
            'code-91': 'Tên đã tồn tại!',
            'code-92': 'Slug đã tồn tại!',
            'code-111': 'Tên đã tồn tại!',
            'code-112': 'Slug đã tồn tại!',
        }
    },
    table: {
        col: {
            edit: 'Sửa',
            delete: 'Xóa',
        }
    },
    buttons: {
        add: 'Thêm mới',
        save: 'Lưu',
        cancel: 'Hủy',
        edit: 'Sửa',
        delete: 'Xóa',
        reset: 'Làm mới',
        filter: 'Lọc',
    },
    notifications: {
        getDataFailed: 'Lấy dữ liệu thất bại!',
    }
}