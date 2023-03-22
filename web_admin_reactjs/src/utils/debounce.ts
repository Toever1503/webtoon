export default function debounce(func: Function, delay: number = 500): Function {
    let debounceTimer: number;
    return function (): void {
        // @ts-ignore
        const context = this
        const args = arguments
        clearTimeout(debounceTimer)
        debounceTimer = setTimeout(() => func.apply(context, args), delay)
    };
};
