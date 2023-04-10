export default {
    'test': 'Ví dụ',
    manga: {
        form:
        {
            'sure-delete': 'Bạn chăc chắn muốn xóa manga này?',
            errors: {
                'check-again': 'Vui lòng kiểm tra lại thông tin!',
                'title-required': 'Tiêu đề không được để trống!',
                'title-max': 'Tiêu đề không được quá 255 ký tự!',
                'content-required': 'Vui lòng nhập nội dung',
                'genres-required': 'GVui lòng chọn thể loại',
                'authors-required': 'Vui lòng chọn tác giả',
                'tags-required': 'Vui lòng chọn thẻ',
                'featured-image-required': 'Vui lòng chọn ảnh',
                'volume-required': 'Please enter volume name!',
                'chapter-name-required': 'Chapter name is required',
                'chapter-images-required': 'Chapter image is required',
                'chapter-content-required': 'Chapter content is required',
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
                    'exceed-character': 'Số ký tự không được vượt quá 30000 ký tự!',
                    'only-txt-allowed': 'Chỉ được tải file txt!',
                }

            }
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
        table: {
            'fullName': 'Họ và tên',
            'username': 'Tên đăng nhập',
            'email': 'Email',
            'phone': 'Số điện thoại',
            'sex': 'Giới tính',
            'avatar': 'Ảnh đại diện',
            'status': 'Trạng thái',
            'createdAt': 'Ngày tạo',
            'accountType': 'Loại tài khoản',
            'action': 'Hành động'
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

            'sure-delete': 'Bạn chắc chắn muốn xóa người dùng này?',
            'delete-success': 'Xóa thành công!',
            'delete-failed': 'Xóa thất bại!',
            'sex-radio': {
                male: 'Nam',
                female: 'Nữ',
            },
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
    },
    'subscription-pack': {
        'page-title': 'Danh sách gói đọc',
        table: {
            'name': 'Tên gói',
            'desc': 'Mô tả',
            'price': 'Giá',
            'limitedDayCount': 'Số ngày đọc',
            'createdAt': 'Ngày tạo',
            'modifiedAt': 'Ngày sửa',
            'createdBy': 'Người tạo',
            'modifiedBy': 'Người sửa',
            'action': 'Hành động'

        }
    },

    'confirm-yes': 'Có',
    'confirm-no': 'Không',
    'reset': 'Làm mới'
}