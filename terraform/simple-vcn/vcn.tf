
/*
 * Network
 */

// VCN
// https://github.com/oracle/terraform-provider-oci/blob/master/docs/core/vcns.md
resource "oci_core_vcn" "ExampleVCN" {
	display_name = "Example VCN"
	compartment_id = "${var.compartment_id}"
	cidr_block = "172.16.0.0/16"
	dns_label = "vcn1"
}

// Internet Gateway
// https://github.com/oracle/terraform-provider-oci/blob/master/docs/core/internet_gateways.md
resource "oci_core_internet_gateway" "ExampleIG" {
	display_name = "Example Internet Gateway"
	compartment_id = "${var.compartment_id}"
	vcn_id = "${oci_core_vcn.ExampleVCN.id}"
}

// Route Table
// https://github.com/oracle/terraform-provider-oci/blob/master/docs/core/route_tables.md
// https://github.com/oracle/terraform-provider-oci/blob/master/docs/Managing%20Default%20Resources.md
resource "oci_core_default_route_table" "DefaultRT" {
	manage_default_resource_id = "${oci_core_vcn.ExampleVCN.default_route_table_id}"

	route_rules {
		network_entity_id = "${oci_core_internet_gateway.ExampleIG.id}"
		destination = "0.0.0.0/0"
	}
}

// Security List
// https://github.com/oracle/terraform-provider-oci/blob/master/docs/core/security_lists.md
resource "oci_core_security_list" "AllowSSH" {
	display_name = "Allow SSH"
	compartment_id = "${var.compartment_id}"
	vcn_id = "${oci_core_vcn.ExampleVCN.id}"

	egress_security_rules {
		destination = "0.0.0.0/0"
		protocol = "all"
	}

	// Allow inbound ssh
	ingress_security_rules {
		protocol = "6" // tcp
		source = "0.0.0.0/0"
		stateless = false
		
		tcp_options {
			"min" = 22
			"max" = 22
		}
	}
}

// Subnet
// https://github.com/oracle/terraform-provider-oci/blob/master/docs/core/subnets.md
resource "oci_core_subnet" "ExamplePublicSubnet" {
	display_name = "Example Public Subnet"
	compartment_id = "${var.compartment_id}"
	vcn_id = "${oci_core_vcn.ExampleVCN.id}"
	availability_domain = "${var.AvailabilityDomain[var.region]}"
	cidr_block = "172.16.1.0/24"
	security_list_ids = ["${oci_core_security_list.AllowSSH.id}"]
}
