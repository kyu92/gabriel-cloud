export default {
    orderByProperty: (arr, prop, reverse= false) => {
        function compare(prop) {
            return (a, b) => {
                let value1 = a[prop],
                    value2 = b[prop];
                return reverse ? value2 - value1 : value1 - value2;
            }
        }
        return arr.sort(compare(prop));
    }
}