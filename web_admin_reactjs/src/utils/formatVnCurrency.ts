function formatVnCurrency(value: number) : string {
    return Intl.NumberFormat('vi-VN', {style: 'currency', currency: 'VND'}).format(value);
};

export default formatVnCurrency;