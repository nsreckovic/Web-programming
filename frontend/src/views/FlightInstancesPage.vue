<template>
    <b-card style="max-width: 50%;" class="ml-auto mr-auto mt-3">
        <b-row class="ml-auto mb-3">
            <b-card-title>
                <h1>Flight Instances</h1>
            </b-card-title>
        </b-row>

        <b-alert v-model="showDismissibleAlert" :variant="variant" dismissible class="my-2">
            {{message}}
        </b-alert>

        <b-row class="ml-auto">
            <h4>All flight instances:</h4>
            <b-table show-empty
                     small
                     stacked="md"
                     :items="table_flight_instances"
                     :fields="flight_instances_fields"
                     striped
                     hover
                     class="text-center">

                <template v-slot:cell(edit)="row">
                    <b-button size="sm" @click="editFlightInstance(row.item)">Edit</b-button>
                </template>

                <template v-slot:cell(delete)="row">
                    <b-button size="sm" variant="danger" @click="deletePrompt(row.item, row.index, $event.target)">
                        Delete
                    </b-button>
                </template>

            </b-table>
        </b-row>

        <b-modal :id="infoModal.id" hide-footer hide-header centered size="lg" :title="infoModal.title"
                 @hide="resetInfoModal">
            <h4 align="center">Are you sure you want to delete this flight instance?</h4>
            <div class="text-center mt-5">
                <b-button class="mr-1" variant="outline-secondary" @click="hideModal">Cancel</b-button>
                <b-button class="mr-1" variant="danger" @click="deleteFlightInstance">Delete</b-button>
            </div>
        </b-modal>

    </b-card>
</template>

<script>
    import FlightInstanceService from "@/services/flightInstance.service";

    export default {
        name: "FlightInstancesPage",
        data() {
            return {
                // Flights
                table_flight_instances: [],
                flight_instances_fields: [
                    {key: 'flightInstance_ID', thClass: 'd-none', tdClass: 'd-none'},
                    {key: 'flight_ID', label: 'Flight'},
                    {key: 'date'},
                    {key: 'Edit'},
                    {key: 'Delete'},
                ],

                // Notification
                successful: false,
                variant: 'warning',
                message: '',
                showDismissibleAlert: false,
                success_message: '',

                infoModal: {
                    id: 'delete-modal',
                    content: ''
                }
            }
        },
        methods: {
            fetchFlightInstances() {
                this.table_flight_instances = []
                FlightInstanceService.getAllFlightInstances().then(
                    response => {
                        response.data.forEach((flight_instance) => {
                            this.table_flight_instances.push({
                                flightInstance_ID: flight_instance.flightInstance_ID,
                                flight_ID: flight_instance.flight_ID,
                                date: flight_instance.date,
                                edit: null,
                                delete: null
                            })
                        })
                    },
                    error => {
                        this.message = error.response.data.message
                        this.showDismissibleAlert = true
                        this.successful = false;
                    }
                )
            },
            editFlightInstance(data) {
                this.$router.push({name: 'editFlightInstance', params: data})
            },
            resetInfoModal() {
                this.infoModal.content = null
            },
            hideModal() {
                this.$root.$emit('bv::hide::modal', this.infoModal.id)
            },
            deletePrompt(item, index, button) {
                this.infoModal.content = item
                this.$root.$emit('bv::show::modal', this.infoModal.id, button)
            },
            deleteFlightInstance() {
                FlightInstanceService.deleteFlightInstance(this.infoModal.content.flightInstance_ID).then(
                    response => {
                        if (response.status == 200) {
                            this.table_flight_instances = []
                            this.hideModal()
                            this.fetchFlightInstances()
                        }
                    }, error => {
                        if (error.response) this.message = error.response.data.message || error.message
                        else this.message = error.toString()
                        this.variant = "danger"
                        this.showDismissibleAlert = true
                        this.successful = false;
                    }
                )
            },
            currentUser() {
                return this.$store.state.auth.user;
            },
        },
        computed: {
            isAdminLoggedIn() {
                if (this.currentUser() && this.currentUser().type) {
                    if (this.currentUser().type === 'ADMIN') {
                        return true;
                    }
                }
                return false;
            }
        },
        mounted() {
            if (!this.isAdminLoggedIn) {
                this.$router.push('/login');
            }
            this.fetchFlightInstances()
        }
    }
</script>

<style scoped>

</style>