function formatVnCurrency(value: number | undefined): string {
    if (!value) value = 0;
    return Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);
};

export default formatVnCurrency;