
const reIndexTbl = (page: number, size: number, data: any[]) => {
    return data.map((item, index) => ({
        ...item,
        index: calculateIndexTbl(page, size, index + 1)
    }));
};

const calculateIndexTbl = (page: number, size: number, index: number) => {
    return (page - 1) * size + index;
}

export {
    reIndexTbl,
};