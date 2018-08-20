variable "tenancy_ocid" {}
variable "user_ocid" {}
variable "fingerprint" {}
variable "private_key_path" {}
variable "compartment_id" {}
variable "region" {}
variable "AvailabilityDomain" {
    type = "map"
    default = {
        us-phoenix-1 = "dArW:PHX-AD-1"
    }
}
