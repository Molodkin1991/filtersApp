var app = new Vue({
    el: '#app', data: {
        filterName: '', filterIndex: 0, filters: [], types: [], items: [], exists: null, showModal: true
    }, methods: {
        addItem() {
            this.items.push({name: "", searchCriteria: "", value: "", id: null});
        }, deleteItem(itemIndex) {
            this.items.splice(itemIndex, 1)
        },

        postToServer() {
            if (this.filters.length < 1) {
                this.filters[0] = {id: null, name: this.filterName, listOfCondition: this.items}
            } else {
                this.filters[0]['listOfCondition'] = this.items
                this.filters[0]['name'] = this.filterName
            }
            let headers = new Headers();
            headers.append("Content-Type", "application/json")
            headers.append("Accept", "application/json")
            // Simple POST request with a JSON body using fetch
            const requestOptions = {
                method: "POST", headers: headers, body: JSON.stringify(this.filters)
            };
            console.log(requestOptions)

            fetch("http://localhost:8081/filter/saveFilter", requestOptions)
                .then(data => (this.postId = data.id));
        }
    }, async created() {
        const response = await fetch("http://localhost:8081/filter");
        const fields = await fetch("http://localhost:8081/filter/fields-types");
        this.filters = await response.json();
        this.types = await fields.json();
        if (this.filters.length > 0) {
            this.items = this.filters[0]['listOfCondition'];
            this.filterName = this.filters[0]['name'];
        }
    },
});
